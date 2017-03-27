/**
 * 
 */
package pub.ayada.insight.xmlReader.cfg;

import java.nio.charset.Charset;
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
@XmlType(name = "", propOrder = { "NAME", "PATH", "ENCODING", "BUFFER_RECS", "BLOCK"})
@XmlRootElement(name = "XML_READER")
public class JAXB implements Component {

	@XmlAttribute(required = true, name = "NAME")
	protected String NAME;

	@XmlAttribute(required = true, name = "PATH")
	protected String PATH = "";

	@XmlAttribute(name = "ENCODING")
	protected String ENCODING = Charset.defaultCharset().name();

	@XmlAttribute(name = "BUFFER_RECS")
	protected int BUFFER_RECS = 50000;

	@XmlElement(required = true, name = "BLOCK")
	protected pub.ayada.insight.xmlReader.cfg.BLOCK BLOCK = new BLOCK();


	public JAXB() {
		super();
	}

	public JAXB(String nAME, String pATH, String eNCODING, int bUFFER_RECS,
			pub.ayada.insight.xmlReader.cfg.BLOCK bLOCK) {
		super();
		NAME = nAME;
		PATH = pATH;
		ENCODING = eNCODING;
		BUFFER_RECS = bUFFER_RECS;
		BLOCK = bLOCK;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public String getENCODING() {
		return ENCODING;
	}

	public void setENCODING(String eNCODING) {
		ENCODING = eNCODING;
	}

	public int getBUFFER_RECS() {
		return BUFFER_RECS;
	}

	public void setBUFFER_RECS(int bUFFER_RECS) {
		BUFFER_RECS = bUFFER_RECS;
	}

	public BLOCK getBLOCK() {
		return BLOCK;
	}

	public void setBLOCK(BLOCK bLOCK) {
		BLOCK = bLOCK;
	}

	@Override
	public ArrayList<String> getOutQueueIDs() {
		ArrayList<String> IDs = new ArrayList<>(1);
			IDs.add(getBLOCK().getRECORD().getID());
		return IDs;
	}

	@Override
	public RecordMeta getOutQRecordMeta(String RecID) {
		RecordMeta rm = new RecordMeta();
		if (getBLOCK().getRECORD().getID().equals(RecID)) {
			getBLOCK().getRECORD().getCOLUMN().forEach(c -> {
				rm.addColumn((new String[] { c.getNAME(), c.getDATA_TYPE() }));
			});

		}
		return rm;
	}

	@Override
	public RecordMeta getOutQRecordMeta(int RecIndex) {
		RecordMeta rm = new RecordMeta();
		getBLOCK().getRECORD().getCOLUMN().forEach(c -> {
			rm.addColumn((new String[] { c.getNAME(), c.getDATA_TYPE() }));
		});
		return rm;
	}

	@Override
	public ArrayList<String> getInQueueIDs() {
		return new ArrayList<String>(1);
	}

	@Override
	public String getComponentName() {
		return getNAME();
	}

	@Override
	public ArrayList<String> getParentNames() {
		return new ArrayList<String>(1);
	}
	
}
