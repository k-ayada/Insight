package pub.ayada.insight.xmlReader.cfg;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.xmlReader.cfg.CACHE_TAG_BLOCK;
import pub.ayada.insight.xmlReader.cfg.CACHE_COLUMN;


@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "CACHE_TAG_BLOCK", "CACHE_COLUMN" })
@XmlRootElement(name = "CACHE")
public class CACHE implements Serializable, Cloneable {
	private static final long serialVersionUID = -8532569520247856481L;

	@XmlElement(name = "CACHE_TAG_BLOCK")
	protected ArrayList<CACHE_TAG_BLOCK> CACHE_TAG_BLOCK;// = new ArrayList<COLUMN>();

	@XmlElement(name = "CACHE_COLUMN")
	protected ArrayList<CACHE_COLUMN> CACHE_COLUMN;// = new ArrayList<CACHE_COLUMN>();

	public CACHE() {
	}

	public CACHE(ArrayList<CACHE_TAG_BLOCK> tAG_BLOCK,
			ArrayList<CACHE_COLUMN> cACHE_COLUMN) {
		super();
		CACHE_TAG_BLOCK = tAG_BLOCK;
		CACHE_COLUMN = cACHE_COLUMN;
	}

	public ArrayList<CACHE_TAG_BLOCK> getTAG_BLOCK() {
		return CACHE_TAG_BLOCK;
	}

	public void setTAG_BLOCK(ArrayList<CACHE_TAG_BLOCK> tAG_BLOCK) {
		CACHE_TAG_BLOCK = tAG_BLOCK;
	}

	public ArrayList<CACHE_COLUMN> getCACHE_COLUMN() {
		return CACHE_COLUMN;
	}

	public void setCACHE_COLUMN(ArrayList<CACHE_COLUMN> cOLUMN) {
		CACHE_COLUMN = cOLUMN;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
