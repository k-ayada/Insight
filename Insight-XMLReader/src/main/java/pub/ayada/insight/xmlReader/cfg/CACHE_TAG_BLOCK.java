package pub.ayada.insight.xmlReader.cfg;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "PATH_TO_NODE" })
@XmlRootElement(name = "TAG_BLOCK")
public class CACHE_TAG_BLOCK implements Serializable, Cloneable {

	private static final long serialVersionUID = 2651073307550010425L;

	@XmlAttribute(required = true, name = "NAME")
	protected String NAME;

	@XmlAttribute(required = true, name = "PATH_TO_NODE")
	protected String PATH_TO_NODE;

	public CACHE_TAG_BLOCK() {
		super();
	}

	public CACHE_TAG_BLOCK(String nAME, String pATH_TO_NODE) {
		super();
		NAME = nAME;
		PATH_TO_NODE = pATH_TO_NODE;
	}

	public String getNAME() {
		return NAME;
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

}
