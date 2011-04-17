package net.sf.ponyo.jponyo.stream;

import net.sf.ponyo.jponyo.common.async.Listener;

/**
 * A per-user service to get notified about user movements, as well as stores most recent coordinates for all joints.
 * 
 * @since 0.1
 */
public interface MotionStreamListener extends Listener {
	
	/**
	 * Will be invoked whenever the specific user has moved any joint part.
	 * 
	 * @since 0.1
	 */
	void onMotion(MotionData data);
	
}
