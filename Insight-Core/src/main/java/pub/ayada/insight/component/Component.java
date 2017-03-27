package pub.ayada.insight.component;

import java.lang.reflect.InvocationTargetException;

import pub.ayada.dataStructures.queues.CQueue;

@SuppressWarnings("rawtypes")
public abstract class Component {
	
	 Object component;

	public void init(ComponentMeta ComponentMeta) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		init(ComponentMeta, null);
	}

	public void init(ComponentMeta ComponentMeta, CQueue[] Data) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
/*		Constructor<?>[] constructor = Class.forName(ComponentMeta.getInitClassName())
				              .getConstructors();*/
	}

	public void start_Consumer(CQueue[] DataIn, CQueue ErrOut) {
	}

	public void start_Producer(CQueue[] DataOut, CQueue ErrOut) {
	}

	public void start_ConsumerProducer(CQueue[] DataIn, CQueue[] DataOut, CQueue ErrOut) {
	}

	public void addInQueue(CQueue DataIn) {
	}
	public void addErrQueue(CQueue ErrOut) {
	}
	
	public void addOutQueue(CQueue DataOut) {
	}

}
