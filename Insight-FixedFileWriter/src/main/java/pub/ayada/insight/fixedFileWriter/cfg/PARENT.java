package pub.ayada.insight.fixedFileWriter.cfg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "RECORD_ID" })
@XmlRootElement(name = "PARENT")
public class PARENT {

	@XmlAttribute(required=true,name = "NAME")
	protected String NAME;

	@XmlAttribute(required=true,name = "RECORD_ID")
	protected String RECORD_ID;


	public PARENT() {
		super();
	}

	public PARENT(String nAME, String rECORD_ID) {
		super();
		NAME = nAME;
		RECORD_ID = rECORD_ID;
	}

	public String getNAME() {
		return NAME;
	}

	public void setPARENT(String nAME) {
		NAME = nAME;
	}

	public String getRECORD_ID() {
		return RECORD_ID;
	}

	public void setRECORD_ID(String rECORD_ID) {
		RECORD_ID = rECORD_ID;
	}
}
