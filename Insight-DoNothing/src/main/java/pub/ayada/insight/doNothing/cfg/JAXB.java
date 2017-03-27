package pub.ayada.insight.doNothing.cfg;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.core.Component;
import pub.ayada.insight.core.ds.RecordMeta;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "NAME", "PARENT", "RECORD_ID"})
@XmlRootElement(name = "DO_NOTHING")
public class JAXB implements Component{
	@XmlAttribute(required=true,name = "NAME")
	protected String NAME;
	
	@XmlAttribute(required=true,name = "PARENT")
	protected String PARENT;

	@XmlAttribute(required=true,name = "RECORD_ID")
	protected String RECORD_ID;

	public JAXB() {
		
	}
	public JAXB(String nAME, String pARENT, String rECORD_ID) {
		setNAME(nAME);
		setPARENT(pARENT);
		setRECORD_ID(rECORD_ID);
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
	public void setRECORD_ID(String iNPUT) {
		RECORD_ID = iNPUT;
	}
	@Override
	public ArrayList<String> getInQueueIDs() {
		 ArrayList<String> lst = new ArrayList<String>(1);
		 lst.add(getRECORD_ID());
		 return lst;
	}
	@Override
	public ArrayList<String> getOutQueueIDs() {
		return new ArrayList<String>(1);
	}
	@Override
	public String getComponentName() {
		return getNAME();
	}
	@Override
	public RecordMeta getOutQRecordMeta(String ID) {
		return null;
	}
	@Override
	public RecordMeta getOutQRecordMeta(int index) {
		return null;
	}
	@Override
	public ArrayList<String> getParentNames() {
		 ArrayList<String> lst = new ArrayList<String>(1);
		 lst.add(getPARENT());
		 return lst;
	}
}
