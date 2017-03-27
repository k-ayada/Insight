package pub.ayada.Insight.replicator.cfg;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.core.Component;
import pub.ayada.insight.core.ds.RecordMeta;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "PARENT", "RECORD_ID", "OUT_RECORD" })
@XmlRootElement(name = "REPLICATOR")
public class JAXB implements Component {

	@XmlAttribute(required=true,name = "NAME")
	protected String NAME;

	@XmlAttribute(required=true,name = "PARENT")
	protected String PARENT ;

	@XmlAttribute(required=true,name = "RECORD_ID")
	protected String RECORD_ID ;

	@XmlElement(required=true,name = "OUT_RECORD")
	protected ArrayList<OUT_RECORD> OUT_RECORD;

	public JAXB() {
		super();
	}

	public JAXB(String nAME, String pARENT, String rECORD_ID, ArrayList<OUT_RECORD> oUT_RECORD) {
		super();
		NAME = nAME;
		PARENT = pARENT;
		RECORD_ID = rECORD_ID;
		OUT_RECORD = oUT_RECORD;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getPARENT() {
		return PARENT;
	}

	public void setPARENT(String pARENT) {
		PARENT = pARENT;
	}

	public String getRECORD_ID() {
		return RECORD_ID;
	}

	public void setRECORD_ID(String rECORD_ID) {
		RECORD_ID = rECORD_ID;
	}

	public ArrayList<OUT_RECORD> getOUT_RECORD() {
		return OUT_RECORD;
	}

	public void setOUT_RECORD(ArrayList<OUT_RECORD> oUT_RECORD) {
		OUT_RECORD = oUT_RECORD;
	}

	@Override
	public ArrayList<String> getInQueueIDs() {
		ArrayList<String> res = new ArrayList<String>(1);
		res.add(getRECORD_ID());
		return res;
	}

	@Override
	public ArrayList<String> getParentNames() {
		ArrayList<String> res = new ArrayList<String>(1);
		res.add(getPARENT());
		return res;
	}

	@Override
	public ArrayList<String> getOutQueueIDs() {
		ArrayList<String> res = new ArrayList<String>(1);
		res.add(getRECORD_ID());
		return res;
	}

	@Override
	public String getComponentName() {
		return getNAME();
	}

	@Override
	public RecordMeta getOutQRecordMeta(String ID) {
		RecordMeta rm = new RecordMeta();
		for (OUT_RECORD rec : getOUT_RECORD()) {
			if (rec.getID().equals(ID)) {
				rec.getCOLUMN().forEach(c -> {
					rm.addColumn((new String[] { c.getNAME(), c.getDATA_TYPE() }));
				});
			}
		}
		return rm;		
	}
	@Override
	public RecordMeta getOutQRecordMeta(int index) {
		RecordMeta rm = new RecordMeta();
		this.getOUT_RECORD().get(index).getCOLUMN().forEach(c -> {
			rm.addColumn((new String[] { c.getNAME(), c.getDATA_TYPE() }));
		});
		return rm;
	}

}
