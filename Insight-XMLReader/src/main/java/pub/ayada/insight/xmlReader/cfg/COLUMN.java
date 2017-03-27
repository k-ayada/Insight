package pub.ayada.insight.xmlReader.cfg;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "SRC", "CACHE_ENTRY","DATA_TYPE", "FORMAT", "TRIM_TYPE", "IS_ATTRIB", "XPATH" })
@XmlRootElement(name = "COLUMN")
public class COLUMN implements Serializable {
	private static final long serialVersionUID = -256859880951902149L;

	/*
	 * <CLOUMN NAME="" START_POS="" LENGTH="" DATA_TYPE=""> </COLUMN>
	 */
	@XmlAttribute(required = true, name = "NAME")
	protected String NAME;

	@XmlAttribute(required = true, name = "SRC")
	protected String SRC;

	@XmlAttribute(required = true, name = "CACHE_ENTRY")
	protected String CACHE_ENTRY;

	@XmlAttribute(name = "DATA_TYPE")
	protected String DATA_TYPE = "STRING";

	@XmlAttribute(name = "FORMAT")
	protected String FORMAT = "";

	@XmlAttribute(name = "TRIM_TYPE")
	protected String TRIM_TYPE = "";

	@XmlAttribute(name = "IS_ATTRIB")
	protected boolean IS_ATTRIB;

	@XmlAttribute(required = true, name = "XPATH")
	protected String XPATH;

	public COLUMN() {
	}

	public COLUMN(String nAME, String sRC, String cACHE_ENTRY, String dATA_TYPE, String fORMAT, String tRIM_TYPE,
			boolean iS_ATTRIB, String xPATH) {
		super();
		NAME = nAME;
		SRC = sRC;
		CACHE_ENTRY = cACHE_ENTRY;
		DATA_TYPE = dATA_TYPE;
		FORMAT = fORMAT;
		TRIM_TYPE = tRIM_TYPE;
		IS_ATTRIB = iS_ATTRIB;
		XPATH = xPATH;
	}


	public String getNAME() {
		return this.NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getSRC() {
		return SRC;
	}

	public void setSRC(String sRC) {
		SRC = sRC;
	}

	
	public String getCACHE_ENTRY() {
		return CACHE_ENTRY;
	}

	public void setCACHE_ENTRY(String cACHE_ENTRY) {
		CACHE_ENTRY = cACHE_ENTRY;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE.toUpperCase();
	}

	public String getFORMAT() {
		return FORMAT;
	}

	public void setFORMAT(String fORMAT) {
		FORMAT = fORMAT;
	}

	public void setTRIM_TYPE(String tRIM_TYPE) {
		this.TRIM_TYPE = tRIM_TYPE.isEmpty() ? "None" : tRIM_TYPE.toUpperCase();
	}

	public String getTRIM_TYPE() {
		return this.TRIM_TYPE;
	}

	public String getXPATH() {
		return XPATH;
	}

	public void setXPATH(String xPATH) {
		XPATH = xPATH;
	}

	public boolean isIS_ATTRIB() {
		return IS_ATTRIB;
	}

	public void setIS_ATTRIB(boolean iS_ATTRIB) {
		IS_ATTRIB = iS_ATTRIB;
	}

}
