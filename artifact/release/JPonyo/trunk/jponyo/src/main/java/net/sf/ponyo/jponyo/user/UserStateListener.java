package net.sf.ponyo.jponyo.user;

import net.sf.ponyo.jponyo.common.async.Listener;

/**
 * @since 0.1
 */
public interface UserStateListener extends Listener {

	void onUserNew(User newUser);
	
	void onUserCalibrationStarted(User user);
	
	void onUserCalibrationFailed(User user);
	
	/**
	 * An user is now being tracked and joint data will be available soon.
	 * 
	 * Could also been have named "onCalibrationSuccessfully", as the underlying code starts the tracking
	 * as soon as the calibration was completed. To simplify things, it was decided to merge these two events ;)
	 * 
	 * @param user which is now being tracked.
	 * @since 0.1
	 */
	void onUserTracking(User user);
	
	void onUserLost(User user);
	
}
