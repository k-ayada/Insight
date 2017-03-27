package pub.ayada.Insight.replicator.cfg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "SOURCE", "DATA_TYPE" })
@XmlRootElement(name = "COLUMN")
public class COLUMN {

	@XmlAttribute(required=true,name = "NAME")
	protected String NAME;

	@XmlAttribute(required=true,name = "SOURCE")
	protected String SOURCE;
	@XmlAttribute(required=true,name = "DATA_TYPE")
	protected String DATA_TYPE;

	public COLUMN() {
		super();
	}

	public COLUMN(String nAME, String sOURCE, String dATA_TYPE) {
		super();
		NAME = nAME;
		SOURCE = sOURCE;
		DATA_TYPE = dATA_TYPE;
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
		DATA_TYPE = dATA_TYPE;
	}
}
