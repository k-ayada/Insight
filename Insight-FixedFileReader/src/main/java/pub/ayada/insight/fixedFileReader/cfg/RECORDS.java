package pub.ayada.insight.fixedFileReader.cfg;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pub.ayada.insight.fixedFileReader.cfg.RECORD;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {"WRITE_IGNORED_REC","RECORD"})
@XmlRootElement(name = "RECORDS")
public class RECORDS {

	@XmlAttribute(name="WRITE_IGNORED_REC" )
	protected boolean WRITE_IGNORED_REC=false;
	
	@XmlElement(name="RECORD" )
	protected ArrayList<RECORD> RECORD;

	public RECORDS() {
	}

	public RECORDS(boolean WRITE_IGNORED_REC, ArrayList<RECORD> RECORD) {
		this.WRITE_IGNORED_REC = WRITE_IGNORED_REC;
		this.RECORD = RECORD;
	}

	public boolean getWRITE_IGNORED_REC() {
		return WRITE_IGNORED_REC;
	}

	public void setWRITE_IGNORED_REC(boolean wRITE_IGNORED_REC) {
		WRITE_IGNORED_REC = wRITE_IGNORED_REC;
	}

	public ArrayList<RECORD> getRECORD() {
		if (this.RECORD == null) return new ArrayList<RECORD>();
	    return this.RECORD;
	}
	
	public void addRECORD(RECORD REC) {
		this.RECORD.add(REC);
	}
    public void removeRECORD(String Id) {    	
        for (int i=0; i< this.RECORD.size(); i++) {
        	if (Id.equals(this.RECORD.get(i).getID())) {
      		   this.RECORD.remove(i);
      		   break;
        	}
		}    	    
    }
}
