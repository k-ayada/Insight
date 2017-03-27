package pub.ayada.insight.xmlReader.cfg;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "PATH_TO_NODE", "ATTRIB" })
@XmlRootElement(name = "COLUMN")
public class CACHE_COLUMN implements Serializable {
	private static final long serialVersionUID = -256859880951902149L;

	/*
	 * <CLOUMN NAME="" START_POS="" LENGTH="" DATA_TYPE=""> </COLUMN>
	 */
	@XmlAttribute(required = true, name = "NAME")
	protected String NAME;	
	
	@XmlAttribute(name = "ATTRIB")
	protected String ATTRIB;

	@XmlAttribute(required=true,name = "PATH_TO_NODE")
	protected String PATH_TO_NODE;



	public CACHE_COLUMN() {
	}

	public CACHE_COLUMN(String nAME, String pATH_TO_NODE, String aTTRIB) {
		super();
		NAME = nAME;
		PATH_TO_NODE = pATH_TO_NODE;
		ATTRIB = aTTRIB;
	}

	public String getNAME() {
		return this.NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}	

	public String getPATH_TO_NODE() {
		return PATH_TO_NODE;
	}

	public void setPATH_TO_NODE(String pATH_TO_NODE) {
		PATH_TO_NODE = pATH_TO_NODE;
	}

	public String getATTRIB() {
		return ATTRIB;
	}

	public void setATTRIB(String aTTRIB) {
		ATTRIB = aTTRIB;
	}

}
