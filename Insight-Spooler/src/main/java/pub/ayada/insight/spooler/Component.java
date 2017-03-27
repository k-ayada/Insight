package pub.ayada.insight.spooler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pub.ayada.dataStructures.queues.CQueue;
import pub.ayada.insight.core.ds.Record;
import pub.ayada.insight.core.stats.ExeStats;
import pub.ayada.insight.spooler.cfg.JAXB;

public class Component implements Runnable {

	static Logger L = LoggerFactory.getLogger(Component.class);
	private CQueue<Record> inQ;
	private ExeStats exeStatsObj;
	private JAXB componentMeta;
	public Component() {
	}	
	public Component(JAXB ComponentPropsXML, ExeStats ExeStats, HashMap<String, HashMap<String, CQueue<Record>>> Qs) throws Exception {		
		this.componentMeta = ComponentPropsXML;
		this.inQ = Qs.get(this.componentMeta.getPARENT()).get(this.componentMeta.getInQueueIDs().get(0));		
		this.exeStatsObj = ExeStats;
	}
	
	/**
	 * Kickoff the Component processing.
	 * 
	 */
	@Override
	public void run() {
		boolean failed = false;
		try {
			while (true) {
				if (  this.inQ.canGet() 
				   || this.inQ.isLoadEnded()
				   || this.exeStatsObj.getStepStats(this.componentMeta.getPARENT()).getStepStatus() < 0) {
				  break;
				}  
				Thread.sleep(999);
			}
			
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(1);
			runContinue();
			
			/* Don't stop the thread until the parent is completed.
			 * No real advantage other than better logging 			
			 */
			while (true) {
				if (this.exeStatsObj.getStepStats(this.componentMeta.getPARENT()).getStepStatus() == 1) {
					Thread.sleep(999);
				} else {
					break;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			Thread.dumpStack();
			failed= true;
		} finally {
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(failed ? -2: 2);
			this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
			Thread.yield();
		}
	}


	private void runContinue() throws Exception {
		long recCount = 0;
		while (true) {
			if (this.inQ.isLoadEnded() && this.inQ.getCount() <= 0)
				break;
			Record rec = this.inQ.getWhenCan(99, 10);
			if (rec == null){
				continue;
			}	
			//L.info("Data: " + rec.toString());

			if (++recCount % this.exeStatsObj.getReportAfter() == 0) {
				this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2InCount(recCount);
				this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutCount(recCount);
				this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
				recCount = 0;
			}
		}
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2InCount(recCount);
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutCount(recCount);

	}

}
