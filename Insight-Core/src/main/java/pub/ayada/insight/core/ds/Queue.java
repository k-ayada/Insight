package pub.ayada.insight.core.ds;

import pub.ayada.dataStructures.queues.CQueue;

public class Queue<E> extends CQueue<E> {
	private static final long serialVersionUID = -3952115187196292811L;
	RecordMeta rm = new RecordMeta();

	public Queue(String queueID, int CQueue_Length) {
		super(queueID, CQueue_Length, null, false);
	}

	public Queue(String queueID, int CQueue_Length, boolean test) {
		super(queueID, CQueue_Length, null, test);
	}

	public Queue(String queueID, int CQueue_Length, CQueue<E> copyFrmQ) {
		super(queueID, CQueue_Length, copyFrmQ, false);
	}

	public Queue(String newQueueID, CQueue<E> copyFrmQ) {
		super(newQueueID, copyFrmQ.getCount(), copyFrmQ, false);
	}

	public void setRecordMeta(RecordMeta RecordMeta) {		
		this.rm = RecordMeta;
	}

	public RecordMeta getRecordMeta() {
		return rm;
	}
	
}
