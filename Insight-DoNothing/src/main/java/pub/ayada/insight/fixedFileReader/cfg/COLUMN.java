package pub.ayada.insight.fixedFileReader.cfg;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "SKIP_COLUMN","START_POS", "LENGTH", "DATA_TYPE", "FORMAT","TRIM_TYPE" })
@XmlRootElement(name = "COLUMN")
public class COLUMN implements Serializable {
	private static final long serialVersionUID = -256859880951902149L;

	/*
	 * <CLOUMN NAME="" START_POS=""  LENGTH="" DATA_TYPE=""> </COLUMN>
	 */
	@XmlAttribute(required=true,name = "NAME")
	protected String NAME;
	
	@XmlAttribute(name = "SKIP_COLUMN")
	protected boolean SKIP_COLUMN = false;

	@XmlAttribute(name = "START_POS")
	protected int START_POS = 0;

	@XmlAttribute(required=true,name = "LENGTH")
	protected int LENGTH = 0;
	
	@XmlAttribute(name = "DATA_TYPE")
	protected String DATA_TYPE = "string";

	@XmlAttribute(name = "FORMAT")
	protected String FORMAT = "";
	
	@XmlAttribute(name = "TRIM_TYPE")
	protected String TRIM_TYPE = "";	

	public COLUMN() {
	}

	public COLUMN(String name, boolean skip_column, int start_pos, int length, String data_type, String format, String trim_type) {
		setNAME(name);
		setSKIP_COLUMN(skip_column);
		setDATA_TYPE(data_type);
		setFORMAT(format);
		setLENGTH(length);
		setSTART_POS(start_pos);
		setTRIM_TYPE(trim_type);
	}

	public String getNAME() {
		return this.NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	
	public boolean isSKIP_COLUMN() {
		return SKIP_COLUMN;
	}

	public void setSKIP_COLUMN(boolean sKIP_COLUMN) {
		SKIP_COLUMN = sKIP_COLUMN;
	}

	public void setTRIM_TYPE(String tRIM_TYPE) {
		this.TRIM_TYPE = tRIM_TYPE.isEmpty()? "None": tRIM_TYPE.toUpperCase();
	}
	
	public String getTRIM_TYPE() {
		return this.TRIM_TYPE;
	}



	public int getSTART_POS() {
		return START_POS;
	}

	public void setSTART_POS(int sTART_POS) {
		START_POS = sTART_POS;
	}

	public int getLENGTH() {
		return LENGTH;
	}

	public void setLENGTH(int lENGTH) {
		LENGTH = lENGTH;
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



}
