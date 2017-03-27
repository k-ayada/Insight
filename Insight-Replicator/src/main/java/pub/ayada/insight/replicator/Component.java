package pub.ayada.Insight.replicator;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pub.ayada.Insight.replicator.cfg.JAXB;
import pub.ayada.insight.core.ds.Queue;
import pub.ayada.insight.core.ds.Record;
import pub.ayada.insight.core.stats.ExeStats;

public class Component implements Runnable {

	static Logger L = LoggerFactory.getLogger(Component.class);
	private Queue<Record> inQ;
	private HashMap<String, Queue<Record>> outQ;
	private ExeStats exeStatsObj;
	private JAXB componentMeta;

	public Component() {
	}

	public Component(JAXB ComponentPropsXML, ExeStats ExeStats, HashMap<String, HashMap<String, Queue<Record>>> Qs)
			throws Exception {
		this.componentMeta = ComponentPropsXML;
		HashMap<String, Queue<Record>> p = null;
		try {
			p = Qs.get(this.componentMeta.getPARENT());
			this.inQ = p.get(this.componentMeta.getInQueueIDs().get(0));
		} catch (Exception e) {
			if (p == null) {

				Qs.keySet().forEach(k -> L.info(k));

				throw new Exception("Failed to get details of parent: " + this.componentMeta.getPARENT());
			} else {
				throw new Exception("Failed to identify the output Record of : " + this.componentMeta.getPARENT() + "->"
						+ this.componentMeta.getInQueueIDs().get(0));
			}
		}

		this.exeStatsObj = ExeStats;
		setRecordMeta();
		Qs.put(this.componentMeta.getNAME(), this.outQ);
	}

	private void setRecordMeta() {

		this.outQ = new HashMap<String, Queue<Record>>(this.componentMeta.getOUT_RECORD().size());

		for (int i = 0; i < this.componentMeta.getOUT_RECORD().size(); i++) {
			String qID = this.componentMeta.getOUT_RECORD().get(i).getID();
			Queue<Record> q = new Queue<Record>(qID, this.inQ.getQsize());
			q.setRecordMeta(this.componentMeta.getOutQRecordMeta(i));
			this.outQ.put(qID, q);
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).addNewOutQ(qID);
		}
	}

	/**
	 * Kickoff the Component processing.
	 * 
	 */
	@Override
	public void run() {
		try {
			while (true) {
				if (this.inQ.canGet() || this.inQ.isLoadEnded()
						|| this.exeStatsObj.getStepStats(this.componentMeta.getPARENT()).getStepStatus() < 0) {
					break;
				}
				Thread.sleep(999);
			}

			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(1);
			runContinue();

			/*
			 * Don't stop the thread until the parent is completed. No real
			 * advantage other than better logging
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
		} finally {
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).setStepStatus(2);
			this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
			Thread.yield();
		}
	}

	private void runContinue() throws Exception {
		long recCount = 0L;
		int chunkCount = -1;
		long hrec = 0L;
		Record[] recs = new Record[1000];
		while (true) {
			if (this.inQ.isLoadEnded() && this.inQ.getCount() <= 0) {
				break;
			}
			Record rec = this.inQ.getWhenCan(99, 10);
			if (rec == null) {
				continue;
			}
			recCount++;
			recs[++chunkCount] = rec;
			if (chunkCount == 999) {
				pushRecs(recs, chunkCount);
				chunkCount = -1;
				if (recCount - hrec >= this.exeStatsObj.getReportAfter()) {
					this.exeStatsObj.reportStepstats(this.componentMeta.getNAME());
					hrec = recCount;
				}
			}
		}
		if (chunkCount > -1) {
			pushRecs(recs, chunkCount);
		}
		for (String k : this.outQ.keySet()) {
			this.outQ.get(k).setLoadEnded(true);
		}
	}

	private void pushRecs(Record[] recs, int chunkCount) throws Exception {
		for (int i = 0; i <= chunkCount; i++) {
			for (String k : this.outQ.keySet()) {
				this.outQ.get(k).addWhenPossible(recs[i], 99);
			}
		}
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2InCount(chunkCount + 1);
		this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutCount(chunkCount + 1);
		for (String k : this.outQ.keySet()) {
			this.exeStatsObj.getStepStats(this.componentMeta.getNAME()).add2OutQCount(k, (chunkCount + 1));
		}
	}

}
