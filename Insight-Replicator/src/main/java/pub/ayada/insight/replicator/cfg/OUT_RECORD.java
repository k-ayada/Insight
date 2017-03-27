package pub.ayada.Insight.replicator.cfg;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "ID", "COLUMN" })
@XmlRootElement(name = "OUT_RECORD")
public class OUT_RECORD {

	@XmlAttribute(required=true,name = "ID")
	protected String ID;

	@XmlElement(required=true,name = "COLUMN")
	protected ArrayList<COLUMN> COLUMN;

	public OUT_RECORD() {
		super();
	}

	public OUT_RECORD(String iD, ArrayList<pub.ayada.Insight.replicator.cfg.COLUMN> cOLUMN) {
		super();
		ID = iD;
		COLUMN = cOLUMN;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public ArrayList<COLUMN> getCOLUMN() {
		return COLUMN;
	}

	public void setCOLUMN(ArrayList<COLUMN> cOLUMN) {
		COLUMN = cOLUMN;
	}

}
