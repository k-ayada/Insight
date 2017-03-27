package pub.ayada.insight.fixedFileReader.cfg;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.fixedFileReader.cfg.COLUMN;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "ID", "FILTER_EXP", "COLUMN" })
@XmlRootElement(name = "RECORD")
public class RECORD {

	/*
	 * <RECORD ID="" FILTER_EXP="" > <COLUMN> ------- ------- </COLUMN> <COLUMN>
	 * ------- ------- </COLUMN> . . </RECORD
	 */
	@XmlAttribute(name = "ID")
	protected String ID = "REC_" + new Random().nextInt(100);


	@XmlElement(name = "FILTER_EXP")
	protected String FILTER_EXP = "true";

	@XmlElement(name = "COLUMN")
	protected ArrayList<COLUMN> COLUMN;// = new ArrayList<COLUMN>();

	public RECORD() {
	}

	public RECORD(String iD, String fILTER_EXP,
			ArrayList<pub.ayada.insight.fixedFileReader.cfg.COLUMN> cOLUMN) {
		super();
		ID = iD;
		FILTER_EXP = fILTER_EXP;
		COLUMN = cOLUMN;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		this.ID = id;
	}

	public String getFILTER_EXP() {
		return this.FILTER_EXP;
	}

	public void setFILTER_EXP(String filter_exp) {
		this.FILTER_EXP = filter_exp;
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
