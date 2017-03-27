package pub.ayada.insight.xmlReader.cfg;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "ID", "START_ELEMENT", "CACHE", "RECORD" })
@XmlRootElement(name = "BLOCK")
public class BLOCK implements Serializable, Cloneable {
	private static final long serialVersionUID = 9120721064285379203L;

	@XmlAttribute(required = true, name = "ID")
	protected String ID;

	@XmlAttribute(required = true, name = "START_ELEMENT")
	protected String START_ELEMENT;

	@XmlElement(name = "CACHE")
	protected pub.ayada.insight.xmlReader.cfg.CACHE CACHE;

	@XmlElement(name = "RECORD")
	protected pub.ayada.insight.xmlReader.cfg.RECORD RECORD;

	public BLOCK() {
	}

	public BLOCK(String iD, String sTART_ELEMENT, pub.ayada.insight.xmlReader.cfg.CACHE cACHE,
			pub.ayada.insight.xmlReader.cfg.RECORD rECORD) {
		super();
		ID = iD;
		START_ELEMENT = sTART_ELEMENT;
		CACHE = cACHE;
		RECORD = rECORD;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getSTART_ELEMENT() {
		return START_ELEMENT;
	}

	public void setSTART_ELEMENT(String sTART_ELEMENT) {
		START_ELEMENT = sTART_ELEMENT;
	}

	public CACHE getCACHE() {
		return CACHE;
	}

	public void setCACHE(CACHE cACHE) {
		CACHE = cACHE;
	}

	public RECORD getRECORD() {
		return RECORD;
	}

	public void setRECORD(RECORD rECORD) {
		RECORD = rECORD;
	}
}
