package pub.ayada.insight.fixedFileReader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pub.ayada.jEvalExp.Expr;

import pub.ayada.genutils.arrays.ArrayFuncs;
import pub.ayada.genutils.file.DirectByteBuffFileReader;
import pub.ayada.genutils.file.FileUtils;

import pub.ayada.dataStructures.chararray.CharArr;

import pub.ayada.insight.core.Defaults;
import pub.ayada.insight.core.ds.Queue;
import pub.ayada.insight.core.ds.Record;
import pub.ayada.insight.core.stats.ExeStats;
import pub.ayada.insight.fixedFileReader.cfg.COLUMN;
import pub.ayada.insight.fixedFileReader.cfg.JAXB;
import pub.ayada.insight.fixedFileReader.cfg.RECORD;



public class Component implements Runnable {
	private JAXB componentMeta;
	private HashMap<String, Queue<Record>> outQ;
	static Logger L = LoggerFactory.getLogger(Component.class);

	private ArrayList<File> FileListObj;
	private ExeStats exeStatsObj;
	private final char CR = '\n';
	private final char LF = '\r';
	
    private Thread SkipRecordWriter; 
	private ArrayList<Expr> fltrExps = new ArrayList<Expr>();

	public Component() {
	}

	public Component(JAXB ComponentPropsXML, ExeStats ExeStats, HashMap<String, HashMap<String, Queue<Record>>> Qs)
			throws Exception {		
		try {			
			this.exeStatsObj = ExeStats;
			this.componentMeta = ComponentPropsXML;
			setRecordMeta();
			prepare4Exe();
			Qs.put(this.componentMeta.getNAME(), this.outQ);
		}catch(Exception e) {			
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(-2);
			this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
			throw e;
		}
	}

	private void setRecordMeta() throws Exception {
		this.fltrExps = new ArrayList<Expr>(this.componentMeta.getRECORDS().getRECORD().size());
		this.outQ = new HashMap<String, Queue<Record>>(this.componentMeta.getRECORDS().getRECORD().size());
		for (int i = 0; i < this.componentMeta.getRECORDS().getRECORD().size(); i++) {
			String expr = this.componentMeta.getRECORDS().getRECORD().get(i).getFILTER_EXP();
			if (expr == null || expr.trim().isEmpty() || expr.trim().toLowerCase().equals("no")) {
				this.fltrExps.add(i, (Expr) null);
			} else {
				this.fltrExps.add(i, new Expr(expr));
			}
			String qID = this.componentMeta.getRECORDS().getRECORD().get(i).getID();
			Queue<Record> q = new Queue<Record>(qID, this.componentMeta.getBUFFER_RECS());
			q.setRecordMeta(this.componentMeta.getOutQRecordMeta(i));
			this.outQ.put(qID, q);
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).addNewOutQ(qID);
		}
		

	}
	
	private void prepare4Exe() throws Exception {
		this.FileListObj = FileUtils.getFiles(this.componentMeta.getPATH());
		if (this.FileListObj.size() > 0) {
			for (File f : this.FileListObj) {
				if (!f.canRead())
					throw new IOException("We don't have read permission for file:" + f.getAbsolutePath());
			}
		} else {
			throw new IOException("Failed to locate the File(s): " + this.componentMeta.getPATH());
		}				
	}
	private void prepareSkipFile() throws Exception {
		Queue<Record> q = new Queue<Record>("IGNORED", this.componentMeta.getBUFFER_RECS());
		this.outQ.put("IGNORED", q);
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).addNewOutQ("IGNORED");
		String s = Defaults.getTempDir();
		StringBuilder path = new StringBuilder(s);
		int dotInx = this.componentMeta.getPATH().lastIndexOf(".");		
		path.append("/")
		    .append(this.componentMeta.getNAME())
		    .append("_")
		    .append(Defaults.getTimeStamp("yyyyMMddHHmmss"))
		    .append( dotInx < 0 ? "" : this.componentMeta.getPATH().substring(dotInx));
		L.info("Skip File Path: " + path.toString());
		this.SkipRecordWriter = new Thread(new pub.ayada.insight.skipWriter.Component(path.toString(), q, this.componentMeta.getENCODING(), this.componentMeta.getEOL()),
				this.componentMeta.getNAME() + "_SKIP");
		
	}

	/**
	 * Returns the component's properties
	 *
	 * @return ComponentMeta - (JAXBFlatFileReader) - Properties Object of the
	 *         File Reader component
	 */
	public JAXB getComponentMeta() {
		return this.componentMeta;
	}

	/**
	 * Returns the JAXBContext of the File reader component's property.
	 *
	 * @return JAXB Context (JAXBContext) - new instance of the JAXBContext for
	 *         the File reader component
	 * @throws JAXBException
	 */
	public JAXBContext getComponentJAXBcontext() throws JAXBException {
		return JAXBContext.newInstance(JAXB.class);
	}

	/**
	 * Kickoff the Component processing.
	 *
	 */
	@Override
	public void run() {
		try {			
			if (!this.componentMeta.getRECORDS().getWRITE_IGNORED_REC()) {
				runContinue();
			} else {
				prepareSkipFile();
				this.SkipRecordWriter.start();
				runContinue();
				this.SkipRecordWriter.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Thread.dumpStack();
		} finally {
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(2);
			this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
			for (String q : this.outQ.keySet()) {
				this.outQ.get(q).setLoadEnded(true);
			}
			Thread.yield();
		}
	}

	private void runContinue() throws Exception {			
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(1);
		for (File fObj : this.FileListObj) {
			L.info("procesing file " + fObj.getAbsolutePath());

			DirectByteBuffFileReader FileReaderObj = new DirectByteBuffFileReader(fObj.getAbsolutePath(),
					this.componentMeta.getBUFFER_RECS() * this.componentMeta.getMAX_REC_LEN() * 2);
			readFile(FileReaderObj);
			FileReaderObj.cleanNclose();
		}
		for (String key : this.outQ.keySet()) {
			this.outQ.get(key).setLoadEnded(true);
		} 
		this.exeStatsObj.setEndOfInput(true);
	}

	/*
	 * Reads the files one by one and pushes the records to the queue(s)
	 * Separate while loop is coded for 1 vs n queues. This will help to reduce
	 * the number of if executed
	 */
	private void readFile(DirectByteBuffFileReader FileReaderObj) throws Exception {
		long hold = 0L;
		while (!FileReaderObj.isEndOfFile()) {
			CharArr[] arr = getLineArray(FileReaderObj.ReadFileChunkAsCharArr(this.componentMeta.getENCODING()),
					this.componentMeta.getBUFFER_RECS());
			int skipcnt = 0;
			for (int i = 0; i < arr.length; i++) {
				boolean[] key = decideQueue(arr[i]);
				// No processing if we don't have the data in the record or
				// the filter key didn't match
				if (key == null || key.length == 0) {
					skipcnt += 1;
					continue;
				}
				boolean skip = true;
				for (int j = 0; j < key.length; j++) {
					if (key[j]) {
						skip = false;
						Record data = BuildRecord(arr[i], this.componentMeta.getRECORDS().getRECORD().get(j));
						if (data == null) {
							continue;
						}
						String qID = this.componentMeta.getRECORDS().getRECORD().get(j).getID();
						this.outQ.get(qID).addWhenPossible(data, 10);
						this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutQCount(qID, 1);
					}
				}
				if (skip) {
					skipcnt += 1;
					if (this.componentMeta.getRECORDS().getWRITE_IGNORED_REC()) {
						Record r  = new Record(arr[i].getString());
						this.outQ.get("IGNORED").addWhenPossible(r, 10);
					}
				}
				arr[i] = null;
			}
			if (skipcnt > 0) {
				this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2SkipCount(skipcnt);
				this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutQCount("IGNORED", skipcnt);
			}
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutCount(arr.length - skipcnt);
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2InCount(arr.length);
			if (this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).getInCount() - hold > this.exeStatsObj
					.getReportAfter()) {
				this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
				hold = this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).getInCount();
			}

		}
		for (String q : this.outQ.keySet()) {
			this.outQ.get(q).setLoadEnded(true);
		}
	}

	private boolean[] decideQueue(CharArr charArr)
			throws ClassNotFoundException, CloneNotSupportedException, ParseException {

		boolean[] res = new boolean[this.fltrExps.size()];
		int j = -1;
		for (int i = 0; i < this.fltrExps.size(); i++) {
			Expr e = this.fltrExps.get(i);
			if (e == null) {
				res[++j] = true;
			} else {
				e.setValue("rec", charArr.getString());
				res[++j] = ((Boolean) e.resolve()).booleanValue();
			}
		}
		return j == -1 ? (boolean[]) null : res;
	}

	private Record BuildRecord(CharArr argBuff, RECORD rec) throws Exception {
		int fieldsCt = 0;
		Object[] fields = new Object[rec.getCOLUMN().size()];

		for (COLUMN col : rec.getCOLUMN()) {
			if (col.isSKIP_COLUMN()) {
				continue;
			}
			int start = col.getSTART_POS();
			int len = col.getLENGTH();
			try {
				fields[fieldsCt++] = argBuff.subCharArr(start - 1, len).convertTo(col.getDATA_TYPE(), col.getFORMAT());
			} catch (Exception e) {
				throw new Exception("Failed to parse the record Start Pos:" + start + " Length:" + len
						+ " in the Record below \n >" + argBuff.toString() + "<", e);

			}

		}
		return new Record(fields);
	}

	/**
	 * Converts the stream of characters into array of lines based on the EOL
	 * characters.
	 *
	 * @param src
	 * @param approxlines
	 * @return
	 * @throws Exception
	 */
	private CharArr[] getLineArray(CharArr src, int approxlines) throws Exception {
		if (src.getPos() == 0) {
			return null;
		}

		int lines = 0, from = 0, len = 0;
		boolean newLine = false;
		CharArr[] resArray = new CharArr[approxlines / 2];

		for (int i = 0; i < src.getPos(); i++) {

			if (src.charAt(i) == this.CR || src.charAt(i) == this.LF && src.charAt(i + 1) != this.CR) {
				newLine = true;
			} else {
				len++;
			}
			if (newLine) {
				// src.put(i, this.CR); Not including .. Will save couple of
				// bytes of memory:)
				resArray[lines] = new CharArr(src, from, len + 1);
				newLine = false;
				len = 0;
				from = i + 1;
				// If the array is getting filled, increase its length
				if (lines > resArray.length - 2) {
					resArray = ArrayFuncs.handleArraySize(resArray, resArray.length + resArray.length / 10);
				}
				// increment the counter
				lines++;
			}
		}

		if (resArray[lines - 1].lastIndexOf('\n') <= 0) {
			throw new Exception("The buffer did not end with newLine character\n"
					+ "Bytes After last newLine character : " + resArray[lines].toString());
		}
		if (resArray.length > lines) {
			return ArrayFuncs.handleArraySize(resArray, lines);
		}
		return resArray;
	}

	/*
	 * public static void main(String[] args) throws Exception { ExeStats
	 * ExeStats = new ExeStats(); HashMap<String, CQueue<Record>> OutQ = new
	 * HashMap<String, CQueue<Record>>(); Component fr = new Component(new
	 * File("C:\\temp\\TEST.XML"), ExeStats, null, OutQ);
	 *
	 * Thread thFileRead = new Thread(fr, "Read"); try { thFileRead.start(); }
	 * catch (Exception e) { throw new
	 * Exception("Exception in File Read thread", e); }
	 *
	 * while (true) { Record rec = null; if
	 * (OutQ.containsKey("FILE_READER_1.REC1")) { if
	 * (OutQ.get("FILE_READER_1.REC1").isLoadEnded() &&
	 * OutQ.get("FILE_READER_1.REC1").getCount() <= 0) { break; } else { rec =
	 * OutQ.get("FILE_READER_1.REC1").getWhenCan(100, 10); } } if (rec == null)
	 * { continue; } else { System.out.println("\t" + rec.toString()); } }
	 *
	 * }
	 */
}
