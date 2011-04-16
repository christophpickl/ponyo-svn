package net.sf.ponyo.jponyo.user;

/**
 * A more OOP-like version of the original {@link UserChangeListener}, using the approach {@link UserStateListener} instead.
 * 
 * @since 0.1
 */
public class UserListenerAdapter implements UserChangeListener {
	
	private final UserStateListener listener;
	
	public UserListenerAdapter(UserStateListener listener) {
		this.listener = listener;
	}

	public void onUserChanged(User user, UserState state) {
		if(state == UserState.NEW) {
			this.listener.onUserNew(user);
		} else if(state == UserState.CALIBRATION_STARTED) {
			this.listener.onUserCalibrationStarted(user);
		} else if(state == UserState.CALIBRATION_FAILED) {
			this.listener.onUserCalibrationFailed(user);
		} else if(state == UserState.TRACKING) {
			this.listener.onUserTracking(user);
		} else if(state == UserState.LOST) {
			this.listener.onUserLost(user);
		} else {
			throw new RuntimeException("Unhandled user state: " + state + "!");
		}
	}

}
