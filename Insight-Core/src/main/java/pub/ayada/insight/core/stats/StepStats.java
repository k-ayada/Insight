package pub.ayada.insight.core.stats;


import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pub.ayada.genutils.thread.ThreadUtils;

public class StepStats {
	private long InCount;
	private long OutCount;
	private HashMap<String,Long> OutPerQCount;
	private long IgnoreCount;
	private int stepStatus; // 0-Yet to Start, 1-In Progress, 2-Ended.
	private long stepStartTime = 0L;
	private long stepEndTime = 0L;
	private long totCPUTime = 0L;
	static Logger L = LoggerFactory.getLogger(StepStats.class);

	public StepStats() {
		this.stepStartTime = System.nanoTime()/1000000L;
		this.InCount = 0L;
		this.OutCount = 0L;
		this.IgnoreCount = 0L;
		this.stepStatus = 0;
		this.totCPUTime = 0L;
		this.OutPerQCount = new HashMap<String,Long>(3);				
	}

	public long getInCount() {
		return this.InCount ;
	}

	public long getOutCount() {
		return this.OutCount ;
	}

	public long getSkipCount() {
		return this.IgnoreCount ;
	}

	public long getStepStartTime() {
		return this.stepStartTime;
	}

	public long getStepEndTime() {
		return this.stepEndTime;
	}

	public String getOutPerQCount() {		 
		StringBuilder sb = new StringBuilder("   OutQueues-> ");
		if (this.OutPerQCount.size() >0 ) {
			for (String k : this.OutPerQCount.keySet()) {
				sb.append(k).append(String.format(": %10d, ", this.OutPerQCount.get(k).longValue()));			
			}
			sb.setLength(sb.length()-2);
			return sb.toString();
		} else return "";	
	}


	public String getElapsedTime() {
		long elapsed;

		if (this.stepEndTime == 0L){
             elapsed = ((System.nanoTime()/1000000L) - this.stepStartTime);
		}else {
			elapsed = (this.stepEndTime - this.stepStartTime);
		}

		int usec = (int) ((elapsed)/1000)%60;
        int umin = (int) ((elapsed)/(1000*60))%60;
        int uhrs = (int) ((elapsed/ (1000*60*60)) % 24);
        
        if (getStepStatus() == 0 || getStepStatus() == 1) {        	
        	return String.format(" Up Time: %02d:%02d:%02d", uhrs,umin,usec);
        }
        
        int csec = (int) ((this.totCPUTime)/1000)%60;
        int cmin = (int) ((this.totCPUTime)/(1000*60))%60;
        int chrs = (int) ((this.totCPUTime/ (1000*60*60)) % 24);        
        return String.format(" Total -Up Time: %02d:%02d:%02d, CPU Time:%02d:%02d:%02d", uhrs,umin,usec, chrs,cmin,csec);
	}

    public int getStepStatus() {
    	return this.stepStatus ;
    }
	public String getStepStatusDesc() {
		switch (getStepStatus()) {
		case 0:
			return " Status: (0) Yet to start";
		case 1:
			return " Status: (1) Progress";
		case 2:
			return " Status: (2) Finished";
		case -1:
			return " Status: (-1) Ignored";
		case -2:
			return " Status: (-2) Failed";			
		}
		return getStepStatus() + "-Unknown";
	}

	public void add2InCount(long InCount) {
		this.InCount +=InCount;
	}

	public void add2OutCount(long OutCount) {
		this.OutCount +=OutCount;
	}
	public void add2OutQCount(String name,long Count) {
		try{
			this.OutPerQCount.put(name,Long.valueOf(this.OutPerQCount.get(name).longValue() + Count));
		}catch (Exception e){
			L.error("Failedd to identify q:" + name + e.getStackTrace() );
		}
	}	
	public void addNewOutQ(String name) {
		this.OutPerQCount.put(name,new Long(0L));
	}

	public void add2SkipCount(long kipCount) {
		this.IgnoreCount +=kipCount;
	}

	public void setStepStatus(int stepStatus) {
		this.stepStatus = stepStatus;
		if (stepStatus == 2) {
			this.stepEndTime = System.nanoTime()/1000000L;
		}			
		setTotCPUTime(ThreadUtils.getCurrentThreadCPUTime()/1000000L);
	}

	public long getTotCPUTime() {
		return totCPUTime;
	}

	public void setTotCPUTime(long totCPUTime) {
		this.totCPUTime = totCPUTime;
	}
	
}
