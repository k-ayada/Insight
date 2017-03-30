package pub.ayada.insight.fixedFileWriter;

import java.util.HashMap;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pub.ayada.genutils.file.DirectByteBuffFileWriter;
import pub.ayada.genutils.string.StringUtils;

import pub.ayada.insight.core.ds.Queue;
import pub.ayada.insight.core.ds.Record;
import pub.ayada.insight.core.stats.ExeStats;

import pub.ayada.insight.fixedFileWriter.cfg.COLUMN;
import pub.ayada.insight.fixedFileWriter.cfg.JAXB;


public class Component implements Runnable {

	static Logger L = LoggerFactory.getLogger(Component.class);
	private Queue<Record> inQ;
	private ExeStats exeStatsObj;
	private JAXB componentMeta;
	private DirectByteBuffFileWriter fw;
	private String outFormat;
	private int[] sourceColInx;
 
	public Component(JAXB ComponentPropsXML, ExeStats ExeStats, HashMap<String, HashMap<String, Queue<Record>>> Qs) throws Exception {		
		this.componentMeta = ComponentPropsXML;
		this.inQ = Qs.get(this.componentMeta.getPARENT()).get(this.componentMeta.getRECORD_ID());		
		this.exeStatsObj = ExeStats;
		
		// Build the record out format 
		
	}
	
	private void buildRecFormat() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.componentMeta.getCOLUMN().size(); i++) {
			try {
				String fmt = this.componentMeta.getCOLUMN().get(i).getFORMAT();
				sb.append(fmt.replaceAll("%", "%" + (i + 1) + "\\$")).append(this.componentMeta.getDELIMITER());
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Failed to handle the formatting of '"
						+ this.componentMeta.getCOLUMN().get(i).getFORMAT() + "' Build so far:" + sb.toString());
			}
		}
		sb.setLength(sb.length() - this.componentMeta.getDELIMITER().length());
		sb.trimToSize();
		sb.append(this.componentMeta.getEOL());
		this.outFormat = sb.toString();
		L.info("Output Record Format:" + this.outFormat);
	}
	
	/**
	 * Kickoff the Component processing.
	 * 
	 */
	@Override
	public void run() {
		boolean failed = false;
		try {	
			while (true) {
				if (  this.inQ.canGet() 
				   || this.inQ.isLoadEnded()
				   || this.exeStatsObj.getStepStats(this.componentMeta.getPARENT()).getStepStatus() < 0) {
				  break;
				}  
				Thread.sleep(999);
			}
			buildRecFormat();
		    // get the out column's indexes	
			int i = -1;
			this.sourceColInx = new int[this.componentMeta.getCOLUMN().size()];
			for (COLUMN c : this.componentMeta.getCOLUMN()) {
				try {				
					this.sourceColInx[++i] = this.inQ.getRecordMeta().getIndexOfcolumn(((c.getSOURCE() != null) ? c.getSOURCE() :c.getNAME()));
				} catch (Exception e) {
					throw new Exception("Failed to get the index of the column: " + ((c.getSOURCE() != null) ? c.getSOURCE(): c.getNAME() ), e);
				}
			}

/*			
			// Delete the file if exists.
			if (FileUtils.checkIfExists(this.componentMeta.getPATH())){
				L.warn("Out file Exists. Deleting it.");
				FileUtils.delFile(new File(this.componentMeta.getPATH()));
			}
*/			
			// Create the output file handler
			this.fw = new DirectByteBuffFileWriter(this.componentMeta.getPATH()
					                             , this.componentMeta.getMODE()
					                             , this.componentMeta.getENCODING()
					                             , this.componentMeta.getEOL());
			
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(1);
			
			runContinue();
			
			/* Don't stop the thread until the parent is completed.
			 * No real advantage other than better logging 			
			 */
			while (true) {
				if (this.exeStatsObj.getStepStats(this.componentMeta.getPARENT()).getStepStatus() == 1) {
					Thread.sleep(999);
				} else {
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Thread.dumpStack();
			failed = true;
		} finally {
			try {
				this.fw.closeFile();
			} catch (IOException e) {
				System.err.println("Error while closing the file: " +this.componentMeta.getPATH());
				e.printStackTrace();
			}
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(failed ? -2 : 2);
			this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
			Thread.yield();
		}
	}

	private void runContinue() throws Exception {
		L.info("Starting the File writer service. Writing to File: " + this.componentMeta.getPATH() );
		long recCount=0L;
		int chunkCount=-1;
		long hrec=0L;
		Record[] recs = new Record[1000];
        while(true) {
    	   if (this.inQ.isLoadEnded() && this.inQ.getCount() <= 0) {
    		   break;
    	  }
    	  Record rec = this.inQ.getWhenCan(99, 10);    	  
    	  if (rec== null) {
    			continue;  	  
    	  }    	  
    	  recCount++;
    	  recs[++chunkCount] = rec;  	
    	  if (chunkCount == 999){     		
    		  writeRecs(recs,chunkCount);
    		  chunkCount = -1;
        	  if (recCount-hrec >= this.exeStatsObj.getReportAfter()) {
        		  this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
        		  hrec=recCount;
        	  }   
    	  } 	  
       }
       if (chunkCount > -1) {
    	   writeRecs(recs,chunkCount);
       }
	}
	private void writeRecs(Record[] recs, int chunkCount) throws Exception {
		int skip=0;
		
		String s =getFormattedRecord(recs[0]);
		StringBuilder sb = new StringBuilder(s.length() * chunkCount);
		sb.append(s);
		for (int i = 1; i <= chunkCount; i++) {
			sb.append(getFormattedRecord(recs[i]));								
		}	
		try {				
			this.fw.write(ByteBuffer.wrap(sb.toString()
					                        .getBytes(this.componentMeta.getENCODING())));
		} catch (Exception e) {
			skip++;			
		}
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2SkipCount(skip);
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2InCount(chunkCount+1);
    	this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutCount(chunkCount+1-skip);  		  
	}
	
	
	private String getFormattedRecord(Record rec) throws Exception {
		Object[] o = new Object[this.sourceColInx.length];
		int j=-1;
		for (int i :this.sourceColInx) {
			o[++j] =rec.getRecord().getColum(i);			
		}		
		return StringUtils.asFormattedStr(this.outFormat, o);
	}
	
	

}
