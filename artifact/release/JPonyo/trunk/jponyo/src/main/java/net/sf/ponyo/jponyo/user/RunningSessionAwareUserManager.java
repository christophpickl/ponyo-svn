package net.sf.ponyo.jponyo.user;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to be able to handle OSC like data.
 * 
 * @since 0.1
 */
public class RunningSessionAwareUserManager implements UserManager {
	
	private final Map<Integer, User> userByOpenniId = new HashMap<Integer, User>();

	private final UserFactory userFactory = new UserFactory();
	
	private final UserManagerCallback callback;
	
	/**
	 * @param callback not necessarily needed to create artificial logins/logouts (in case of a running OSC session)
	 */
	public RunningSessionAwareUserManager(UserManagerCallback callback) {
		this.callback = callback;
	}

	public User lookupUser(int openniId, int userStateId) {
		
		if(userStateId == UserStateConstants.ID_NEW) {
			User storedUser = this.userByOpenniId.get(Integer.valueOf(openniId));
			if(storedUser != null) {
				// artificial LOGOUT
				
			}
		} else {
			
		}
		
		return null;
	}
	
	public User getUser(int openniId) {
		User storedUser = this.userByOpenniId.get(Integer.valueOf(openniId));
		if(storedUser != null) {
			return storedUser;
		}
		
		// we received joint data, but no user was yet registered => artificially LOGIN
		User user = this.userFactory.create(openniId);
		this.userByOpenniId.put(Integer.valueOf(openniId), user);
		callback.processUserStateChange(user, UserState.NEW);
		callback.processUserStateChange(user, UserState.CALIBRATION_STARTED);
		callback.processUserStateChange(user, UserState.TRACKING);
		
		return user;
	}

}
