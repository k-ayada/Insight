package pub.ayada.insight.fixedFileReader.cfg;

import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.core.Component;
import pub.ayada.insight.core.ds.RecordMeta;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "PATH", "ENCODING", "EOL", "BUFFER_RECS", "MAX_REC_LEN", "RECORDS" })
@XmlRootElement(name = "FILE_READER")
public class JAXB implements Component {
	/*
	 * <FILE_READER NAME="" PATH="" ENCODING="" EOL="" BUFFER_RECS=""> <RECORDS>
	 * <RECORD START="" LENGTH="" VALUE=""> </RECORD> <RECORD START="" LENGTH=""
	 * VALUE=""> </RECORD> </RECORDS> </FILE_READER>
	 */

	@XmlAttribute(required=true,name = "NAME")
	protected String NAME;

	@XmlAttribute(required=true,name = "PATH")
	protected String PATH = "";

	@XmlAttribute(name = "ENCODING")
	protected String ENCODING = Charset.defaultCharset().name();

	@XmlAttribute(name = "EOL")
	protected String EOL = System.getProperty("line.separator");

	@XmlAttribute(name = "BUFFER_RECS")
	protected int BUFFER_RECS = 50000;

	@XmlAttribute(name = "MAX_REC_LEN")
	protected int MAX_REC_LEN = 100;

	@XmlElement(required=true,name = "RECORDS")
	protected RECORDS RECORDS = new RECORDS();

	public JAXB() {
	}	
	
	public JAXB(String nAME, String pATH, String eNCODING, String eOL, int bUFFER_RECS, int mAX_REC_LEN,
			pub.ayada.insight.fixedFileReader.cfg.RECORDS rECORDS) {
		super();
		NAME = nAME;
		PATH = pATH;
		ENCODING = eNCODING;
		EOL = eOL;
		BUFFER_RECS = bUFFER_RECS;
		MAX_REC_LEN = mAX_REC_LEN;
		RECORDS = rECORDS;
	}



	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public String getENCODING() {
		return ENCODING;
	}

	public int getBUFFER_RECS() {
		return this.BUFFER_RECS;
	}

	public String getEOL() {
		return EOL;
	}

	public void setEOL(String eOL) {
		this.EOL = eOL.isEmpty() ? System.getProperty("line.separator") : eOL.replaceAll("\\\\", "\\");
	}

	public void setBUFFER_RECS(int BUFFER_RECS) {
		this.BUFFER_RECS = BUFFER_RECS;
	}

	public void setENCODING(String eNCODING) {
		this.ENCODING = eNCODING.isEmpty() ? Charset.defaultCharset().name() : eNCODING;
	}

	public int getMAX_REC_LEN() {
		return MAX_REC_LEN;
	}

	public void setMAX_REC_LEN(int mAX_REC_LEN) {
		MAX_REC_LEN = mAX_REC_LEN;
	}

	public RECORDS getRECORDS() {
		return RECORDS;
	}

	public void setRECORDS(RECORDS rECORDS) {
		RECORDS = rECORDS;
	}

	@Override
	public ArrayList<String> getOutQueueIDs() {
		ArrayList<String> IDs = new ArrayList<>(this.RECORDS.getRECORD().size());
		for (RECORD rec : this.RECORDS.getRECORD()) {
			IDs.add(rec.getID());
		}
		return IDs;
	}

	@Override
	public RecordMeta getOutQRecordMeta(String RecID) {
		RecordMeta rm = new RecordMeta();
		for (RECORD rec : this.RECORDS.getRECORD()) {
			if (rec.getID().equals(RecID)) {
				rec.getCOLUMN().forEach(c -> {
					rm.addColumn((new String[] { c.getNAME(), c.getDATA_TYPE() }));
				});

			}
		}
		return rm;
	}

	@Override
	public RecordMeta getOutQRecordMeta(int RecIndex) {
		RecordMeta rm = new RecordMeta();
		this.getRECORDS().getRECORD().get(RecIndex).getCOLUMN().forEach(c -> {
			rm.addColumn((new String[] { c.getNAME(), c.getDATA_TYPE() }));
		});
		return rm;
	}

	@Override
	public ArrayList<String> getInQueueIDs() {
		return new ArrayList<String>(1);
	}

	@Override
	public String getComponentName() {
		return getNAME();
	}

	@Override
	public ArrayList<String> getParentNames() {
		return new ArrayList<String>(1);
	}
}
