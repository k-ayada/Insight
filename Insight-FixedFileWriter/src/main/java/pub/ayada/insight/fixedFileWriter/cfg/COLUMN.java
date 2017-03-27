package pub.ayada.insight.fixedFileWriter.cfg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "SOURCE", "DATA_TYPE", "LENGTH","FORMAT", "TRIM_TYPE" })
@XmlRootElement(name = "COLUMN")
public class COLUMN {
	@XmlAttribute(required=true,name = "NAME")
	protected String NAME =null;

	@XmlAttribute(required=true,name = "SOURCE")
	protected String SOURCE =null;

	@XmlAttribute(required=true,name = "DATA_TYPE")
	protected String DATA_TYPE=null;
	
	@XmlAttribute(required=true,name = "LENGTH")
	protected int LENGTH;
	

	@XmlAttribute(name = "FORMAT")
	protected String FORMAT=null;

	@XmlAttribute(name = "TRIM_TYPE")
	protected String TRIM_TYPE=null;

	public COLUMN() {
		super();
	}

	public COLUMN(String nAME, String sOURCE, String dATA_TYPE, int lENGTH, String fORMAT, String tRIM_TYPE) {
		super();
		NAME = nAME;
		SOURCE = sOURCE;
		DATA_TYPE = dATA_TYPE;
		LENGTH = lENGTH;
		FORMAT = fORMAT;
		TRIM_TYPE = tRIM_TYPE;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getSOURCE() {
		return SOURCE;
	}

	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE.toUpperCase();
	}	

	public int getLENGTH() {
		return LENGTH;
	}

	public void setLENGTH(int lENGTH) {
		LENGTH = lENGTH;
	}

	public String getFORMAT() {
		
		return this.FORMAT.isEmpty() ? getDefaultFormat() : FORMAT;
	}

	private String getDefaultFormat() {

/*		switch (getDATA_TYPE()) {
		case "STR":
		case "CHR":
			return "%1s";
		case "INT":
		case "LON":
			return "##0";
		case "FLO":
		case "DEC":
			return "#0.00";
		case "DAT":
			return "yyy-MM-dd HH:mm:ss z";
		default:
			throw new IllegalArgumentException(
					"Failed to determine the default formatting. Pelease define a format for column" + getNAME());
		}
		*/
		
		switch (getDATA_TYPE().substring(0, 3)) {
		case "STR":
		case "CHR":
			return (getLENGTH() > 0) ? "%"+ getLENGTH()+"s" :"%s";
		case "INT":
		case "LON":
			return (getLENGTH() > 0) ? "%0"+ getLENGTH()+"d" :"%d";
		case "FLO":
		case "DEC":
			return (getLENGTH() > 0) ? "%0"+ getLENGTH()+".2f" :"%0.2f";
		case "DAT":
			return "%td";
		case "TIM":
			return "%td %tT %tz";			
		default:
			throw new IllegalArgumentException(
					"Failed to determine the default formatting. Pelease define a format for column " + getNAME() + " of Type:" + getDATA_TYPE() + " (" + getDATA_TYPE().substring(1, 3) + ")");
		}
		
		
	}

	public void setFORMAT(String fORMAT) {
		FORMAT = fORMAT.trim();
	}

	public String getTRIM_TYPE() {
		return TRIM_TYPE;
	}

	public void setTRIM_TYPE(String tRIM_TYPE) {
		TRIM_TYPE = tRIM_TYPE;
	}

}
