package pub.ayada.insight.fixedFileWriter.cfg;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.core.Component;
import pub.ayada.insight.core.ds.RecordMeta;

import pub.ayada.insight.fixedFileWriter.cfg.COLUMN;
import pub.ayada.insight.fixedFileWriter.cfg.PARENT;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "PATH", "MODE", "DELIMITER",  "ENCODING", "EOL", "HEADER", "PARENT", "COLUMN" })
@XmlRootElement(name = "FILE_WRITER")
public class JAXB implements Component {

	@XmlAttribute(required=true,name = "NAME")
	protected String NAME = "FILE_WRITER_" + new Random().nextInt(100);

	@XmlAttribute(required=true,name = "PATH")
	protected String PATH = "";		
	
	@XmlAttribute(name = "MODE")
	protected String MODE = "create";
	
	@XmlAttribute(name = "DELIMITER")
	protected String DELIMITER = "";

	@XmlAttribute(name = "ENCODING")
	protected String ENCODING = Charset.defaultCharset().name();

	@XmlAttribute(name = "EOL")
	protected String EOL = System.getProperty("line.separator");

	@XmlAttribute(name = "HEADER")
	protected boolean HEADER = false;

	@XmlElement(required=true,name = "PARENT")
	protected PARENT PARENT;

	@XmlElement(required=true,name = "COLUMN")
	protected ArrayList<COLUMN> COLUMN = new ArrayList<COLUMN>();

	public JAXB() {
		super();
	}


	public JAXB(String nAME, String pATH, String mODE, String dELIMITER, String eNCODING, String eOL, boolean hEADER,
			PARENT pARENT, ArrayList<COLUMN> cOLUMN) {
		super();
		NAME = nAME;
		PATH = pATH;
		MODE = mODE;
		DELIMITER = dELIMITER;
		ENCODING = eNCODING;
		EOL = eOL;
		HEADER = hEADER;
		PARENT = pARENT;
		COLUMN = cOLUMN;
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

	public String getMODE() {
		return MODE;
	}

	public void setMODE(String mODE) throws IllegalArgumentException {

		switch (mODE.toLowerCase()) {
		case "create":
		case "append":
			this.MODE = mODE;
			break;
		default:
			throw new IllegalArgumentException(
					"Invalid mode defined while opening the file :" + mODE + "valid modes: c[create] | a[apend])");

		}
	}	

	public String getDELIMITER() {
		return DELIMITER;
	}


	public void setDELIMITER(String dELIMITER) {
		DELIMITER = dELIMITER.replaceAll("\\\\", "\\");
	}


	public String getENCODING() {
		return ENCODING;
	}

	public void setENCODING(String eNCODING) {			
		try {
		    Charset.forName(eNCODING);
		    ENCODING = eNCODING;
		  } catch (IllegalArgumentException e) {
		    throw new IllegalArgumentException("Unsupported Encoding:" + eNCODING, e);
		  }	
	}

	public String getEOL() {
		return EOL;
	}

	public void setEOL(String eOL) {
		EOL = eOL.replaceAll("\\\\", "\\");
	}

	public boolean isHEADER() {
		return HEADER;
	}

	public void setHEADER(boolean hEADER) {
		HEADER = hEADER;
	}

	public PARENT getPARENT() {
		return PARENT;
	}

	public void setPARENT(PARENT pARENT) {
		PARENT = pARENT;
	}
	
	public ArrayList<COLUMN> getCOLUMN() {
		return COLUMN;
	}

	public void setCOLUMN(ArrayList<COLUMN> cOLUMN) {
		COLUMN = cOLUMN;
	}

	@Override
	public ArrayList<String> getInQueueIDs() {
		ArrayList<String> res = new ArrayList<String>(1);
		res.add(getPARENT().getRECORD_ID());
		return res;
	}

	@Override
	public ArrayList<String> getParentNames() {
		ArrayList<String> res = new ArrayList<String>(1);
		res.add(getPARENT().getNAME());
		return res;
	}

	@Override
	public ArrayList<String> getOutQueueIDs() {
		return null;
	}

	@Override
	public String getComponentName() {
		return getNAME();
	}

	@Override
	public RecordMeta getOutQRecordMeta(String ID) {
		return null;
	}

	@Override
	public RecordMeta getOutQRecordMeta(int index) {
		return null;
	}

}
