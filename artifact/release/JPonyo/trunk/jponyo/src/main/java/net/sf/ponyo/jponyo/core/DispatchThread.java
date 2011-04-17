package net.sf.ponyo.jponyo.core;

import java.util.concurrent.LinkedBlockingQueue;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 0.1
 */
public class DispatchThread extends DefaultAsync<MotionStreamListener> implements Runnable {

	private static final Log LOG = LogFactory.getLog(DispatchThread.class);
	
	private final Thread dispatcherThread = new Thread(this, "DispatchThread");
	private final LinkedBlockingQueue<MotionData> jointQueue;
	private boolean shouldRun = true;
	
//	private final DefaultAsync<ConnectionJointListener> listeners = new DefaultAsync<ConnectionJointListener>();
	
	public DispatchThread(LinkedBlockingQueue<MotionData> jointQueue) {
		this.jointQueue = jointQueue;
	}
	
	public void start() {
		LOG.debug("start()");
		this.dispatcherThread.start();
	}
	
	public void run() {
		LOG.debug("run() START");
		while(this.shouldRun == true) {
			try {
				MotionData data = this.jointQueue.take();
				this.dispatch(data);
			} catch (InterruptedException e) {
				LOG.debug("Interrupted while taking from joint queue.");
			}
		}
		LOG.debug("run() END");
	}

	private void dispatch(MotionData data) {
		for (MotionStreamListener listener : this.getListeners()) {
			listener.onMotion(data);
		}
	}

	public void stop() throws InterruptedException {
		LOG.debug("stop()");
		this.shouldRun = false;
		this.dispatcherThread.interrupt();
		this.dispatcherThread.join();
	}

}
