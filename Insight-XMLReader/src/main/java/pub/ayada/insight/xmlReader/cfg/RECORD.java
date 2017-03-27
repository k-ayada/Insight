package pub.ayada.insight.xmlReader.cfg;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.xmlReader.cfg.COLUMN;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "ID", "PATH_TO_NODE", "COLUMN" })
@XmlRootElement(name = "RECORD")
public class RECORD {

	@XmlAttribute(required = true, name = "ID")
	protected String ID = "REC_" + new Random().nextInt(100);

	@XmlAttribute(required = true, name = "PATH_TO_NODE")
	protected String PATH_TO_NODE;

	@XmlElement(required = true, name = "COLUMN")
	protected ArrayList<COLUMN> COLUMN;// = new ArrayList<COLUMN>();

	public RECORD() {
	}

	public RECORD(String iD, String pATH_TO_NODE, ArrayList<COLUMN> cOLUMN) {
		super();
		ID = iD;
		PATH_TO_NODE = pATH_TO_NODE;
		COLUMN = cOLUMN;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		this.ID = id;
	}

	public String getPATH_TO_NODE() {
		return this.PATH_TO_NODE;
	}

	public void setPATH_TO_NODE(String pATH_TO_NODE) {
		this.PATH_TO_NODE = pATH_TO_NODE;
	}

	public void setCOLUMN(ArrayList<COLUMN> cOLUMN) {
		COLUMN = cOLUMN;
	}

	public ArrayList<COLUMN> getCOLUMN() {
		if (this.COLUMN == null)
			return new ArrayList<COLUMN>();
		return this.COLUMN;
	}

	public ArrayList<String> getColumnInOrder() {
		ArrayList<String> InColumnList = new ArrayList<String>(getCOLUMN().size());

		for (COLUMN c : getCOLUMN())
			InColumnList.add("$" + c.getNAME());

		return InColumnList;
	}

}
