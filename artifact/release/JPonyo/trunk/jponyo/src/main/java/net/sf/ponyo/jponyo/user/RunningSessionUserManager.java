package net.sf.ponyo.jponyo.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Used to be able to handle OSC like data.
 * 
 * @since 0.1
 */
public class RunningSessionUserManager implements UserManager {
	
	private static final Log LOG = LogFactory.getLog(RunningSessionUserManager.class);
	
	private final Map<Integer, User> userByOpenniId = new HashMap<Integer, User>();

	private final UserFactory userFactory = new UserFactory();
	private final UserManagerCallback callback;
	
	/**
	 * @param callback not necessarily needed to create artificial logins/logouts (in case of a running OSC session)
	 */
	@Inject
	public RunningSessionUserManager(@Assisted UserManagerCallback callback) {
		LOG.debug("new RunningSessionAwareUserManager(callback=" + callback + ")");
		this.callback = callback;
	}

	public User lookupForUserMessage(int openniId, int userStateId) {
		LOG.info("lookupUser(openniId="+openniId+", userStateId="+userStateId+")");
		UserState newState = UserStateConstants.MAP_ID_TO_ENUM[userStateId];
		
		final User result;
		if(newState == UserState.NEW) {
			User newUser = this.userFactory.create(openniId);
			
			User previouslyStoredUser = this.userByOpenniId.put(Integer.valueOf(openniId), newUser);
			if(previouslyStoredUser != null) {
				LOG.warn("overwritten user " + previouslyStoredUser + " ===> artificial LOGOUT");
				previouslyStoredUser.setState(UserState.LOST);
				this.callback.processUserStateChange(previouslyStoredUser, UserState.LOST);
			}
			result = newUser;
			
		} else if(newState == UserState.LOST) {
			User removedUser = this.userByOpenniId.remove(Integer.valueOf(openniId));
			LOG.debug("removed user: " + removedUser);
			
			if(removedUser == null) { // if starting up osc, no joint data, suddenly a user lost message only
				User newUser = this.userFactory.create(openniId);
				User previouslyStoredUser = this.userByOpenniId.put(Integer.valueOf(openniId), newUser);
				assert(previouslyStoredUser == null);
				this.loginUser(newUser);
				result = newUser;
				
			} else {
				assert(removedUser.getOpenniId() == openniId);
				result = removedUser;
			}
			
		} else {
			User storedUser = this.userByOpenniId.get(Integer.valueOf(openniId));
			if(storedUser != null) {
				// ignore user's current state; though this could lead to problems, this case is veeeery rare ;)
				result = storedUser;
				
			} else {
				LOG.warn("we received a non-new user change, but no user was yet registered ===> artificially LOGIN");
				User newUser = this.userFactory.create(openniId);
				this.callback.processUserStateChange(newUser, UserState.NEW);
				result = newUser;
			}
		}
		
		result.setState(newState);
		return result;
	}
	
	public User lookupForJointMessage(int openniId) {
		User storedUser = this.userByOpenniId.get(Integer.valueOf(openniId));
		if(storedUser != null) {
			return storedUser;
		}
		
		LOG.warn("we received joint data, but no user was yet registered ===> artificially LOGIN");
		
		User newUser = this.userFactory.create(openniId);
		this.userByOpenniId.put(Integer.valueOf(openniId), newUser);
		this.loginUser(newUser);
		
		return newUser;
	}
	
	private void loginUser(User newUser) {
		this.callback.processUserStateChange(newUser, UserState.NEW);
		newUser.setState(UserState.CALIBRATION_STARTED);
		this.callback.processUserStateChange(newUser, UserState.CALIBRATION_STARTED);
		newUser.setState(UserState.TRACKING);
		this.callback.processUserStateChange(newUser, UserState.TRACKING);
	}

	@BindingAnnotation
	@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RunningSessionManager {
		// empty - as just an annotation, neo
	}
}
