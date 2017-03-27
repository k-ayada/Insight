package pub.ayada.insight.skipWriter;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pub.ayada.genutils.file.DirectByteBuffFileWriter;
import pub.ayada.insight.core.ds.Queue;
import pub.ayada.insight.core.ds.Record;


public class Component implements Runnable {
	static Logger L = LoggerFactory.getLogger(Component.class);
	private Queue<Record> inQ;
	private DirectByteBuffFileWriter skipRecsWriteObj;
	private String encoding;
	public Component() {
	}	
	public Component(String outPath,Queue<Record> Q, String encoding, String eol) throws Exception {		
		this.skipRecsWriteObj = new DirectByteBuffFileWriter(outPath,"create", encoding, eol);				
		this.skipRecsWriteObj.lockFile();
		this.inQ = Q;
		this.encoding = encoding;
	}
	
	/**
	 * Kickoff the Component processing.
	 * 
	 */
	@Override
	public void run() {
		try {	
			while (true) {
				if (this.inQ.canGet() || this.inQ.isLoadEnded()) 
				  break;
				Thread.sleep(999);
			}
			runContinue();
		} catch (Exception e) {
			e.printStackTrace();
			Thread.dumpStack();
		} finally {
			try {
				skipRecsWriteObj.closeFile();
			} catch (IOException e) {				
			}
			L.info("Ending Thread: " + Thread.currentThread().getName());
			Thread.yield();
		}
	}


	private void runContinue() throws Exception {	 
		L.info("Starting Thread: " + Thread.currentThread().getName());
		long recCount=0L;
		int chunkCount=-1;
		String[] recs = new String[1000];
        while(true) {
    	   if (this.inQ.isLoadEnded() && this.inQ.getCount() <= 0) 
    		   break;   
    	  Record rec = this.inQ.getWhenCan(99, 10);    	  
    	  if (rec== null)
    			continue;  	  
    	  recCount++;
    	  if (chunkCount < 999){ 
    		  recs[++chunkCount] = (String) rec.getColum(0);  
    	  } else {
    		  writeRecs(recs,chunkCount,recCount);
    		  chunkCount = -1;
    		  recs[++chunkCount] = (String) rec.getColum(0);
    	  }
       }
       if (chunkCount >0 ) {
    	   writeRecs(recs,chunkCount,recCount);
       }
	}
	private void writeRecs(String[] recs, int chunkCount, long recCount) throws Exception {
		String s =recs[0];
		StringBuilder sb = new StringBuilder(s.length() * chunkCount);
		sb.append(s);
		for (int i = 1; i <= chunkCount; i++) {
			if (recs[i] != null) {
				sb.append(recs[i]);
			}	
		}
		try {
		  this.skipRecsWriteObj.write(ByteBuffer.wrap(sb.toString().getBytes(this.encoding)));
		} catch (Exception e) {						
		}		
	}
}
