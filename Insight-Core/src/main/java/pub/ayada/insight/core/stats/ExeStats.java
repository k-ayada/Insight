package pub.ayada.insight.core.stats;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pub.ayada.insight.core.ds.Queue;

public class ExeStats {
	private volatile int threadCount;
	private final String LogLevl;
	private final String CharSet;
	private volatile boolean endOfInput;
	private long stepStartNano = 0L;
	private long ReportAfter = 100_000L;
	private HashMap<String, StepStats> StepStats = new HashMap<String, StepStats>();
	private HashMap<String, Thread> StepThreads = new HashMap<String, Thread>();
	private ArrayList<Queue<?>> queueList = new ArrayList<Queue<?>>();
	
	static Logger L = LoggerFactory.getLogger(ExeStats.class);

	public ExeStats() {
		this.LogLevl = "Basic";
		this.CharSet = java.nio.charset.Charset.defaultCharset().name();
		initValues();
	}

	public ExeStats(String LogLevel, String CharSet) {
		this.LogLevl = LogLevel;
		this.CharSet = CharSet;
		initValues();
	}

	private void initValues() {
		this.stepStartNano = System.nanoTime() / 1000000L;
		this.endOfInput = false;
	}

	public boolean isEndOfInput() {
		return this.endOfInput;
	}

	public void setEndOfInput(boolean endOfInput) {
		this.endOfInput = endOfInput;
	}

	public String getCharSet() {
		return this.CharSet;
	}

	public int getThreadCount() {
		return this.threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	
	public HashMap<String, Thread> getStepThreads() {
		return StepThreads;
	}

	public void setStepThreads(HashMap<String, Thread> stepThreads) {
		StepThreads = stepThreads;
	}
	public Thread getStepThread(String stepName) {		
		return this.StepThreads.get(stepName);
	}
	public void addStepThread(String stepName, Thread thread) {
		this.StepThreads.put(stepName, thread);
	}

	public String getLogLevl() {
		return this.LogLevl;
	}

	public void addStepStats(String stepName) {
		this.StepStats.put(stepName, new StepStats());
	}

	public StepStats getStepStats(String stepName) {
		return this.StepStats.get(stepName);

	}
	
	public long getReportAfter() {
		return this.ReportAfter;
	}

	public void setReportAfter(long reportAfter) {
		this.ReportAfter = reportAfter;
	}

	public void reportStepstats(String stepName) {

		StepStats s = getStepStats(stepName);
		L.info(
				/*String.format("%15s",
				             stepName.substring(0,
						              (stepName.length() > 15 ? 
						                  15:stepName.length()
						               ))
						    )+*/
				  String.format("In: %10d", s.getInCount())
				+ String.format(" Skip: %10d", s.getSkipCount())
				+ String.format(" Out: %10d", s.getOutCount())
			
				+ s.getElapsedTime()			
				+ s.getStepStatusDesc()	
				);
		String x = s.getOutPerQCount();
		if(x.length()>0)
			L.info(x);
	}
	public String getElapsedTime() {
		long ElapsedMil = ((System.nanoTime() / 1000000L) - this.stepStartNano);
		int sec = (int) ((ElapsedMil) / 1000) % 60;
		int min = (int) ((ElapsedMil) / (1000 * 60)) % 60;
		int hrs = (int) ((ElapsedMil / (1000 * 60 * 60)) % 24);
		return String.format("%02d:%02d:%02d", hrs, min, sec);

	}
	
	public String getQueuestats() {
		StringBuilder b = new StringBuilder("Queues: ");
		int counter = 0;
		for (Queue<?> queue : this.queueList)
			b.append(String.format("%02d/%02d", this.queueList.size() - 1, ++counter)).append("  ")
					.append(queue.getStats()).append("  ");
		return b.toString();
	}

	public void add2QueueList(Queue<?> queue) {
		this.queueList.add(queue);
	}

}
