package net.sf.ponyo.jponyo.stream;

import net.sf.ponyo.jponyo.common.async.DefaultAsyncFor;
import net.sf.ponyo.jponyo.user.User;

/**
 * @since 0.1
 */
public class MotionStreamImpl
	extends DefaultAsyncFor<User, MotionStreamListener>
		implements MotionStream, MotionStreamListener {
	
	public void onMotion(MotionData data) {
		// indirectly received from dispatcher thread
		for (MotionStreamListener listener : this.getListenersFor(data.getUser())) {
			listener.onMotion(data);
		}
		
	}

}
