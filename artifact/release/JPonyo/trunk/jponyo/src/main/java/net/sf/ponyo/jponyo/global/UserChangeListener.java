package net.sf.ponyo.jponyo.global;

import net.sf.ponyo.jponyo.common.async.Listener;
import net.sf.ponyo.jponyo.entity.User;

public interface UserChangeListener extends Listener {
	
	void onUserNew(User newUser);
	void onUserCalibrationStarted(User user);
	void onUserCalibrationFailed(User user);
	void onUserTracking(User user); // == CalibrationSuccessfully
	void onUserLost(User user);
	
}
