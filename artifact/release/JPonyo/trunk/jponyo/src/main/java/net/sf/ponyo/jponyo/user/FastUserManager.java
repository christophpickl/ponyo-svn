package net.sf.ponyo.jponyo.user;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Processes raw user messages and stores all users by OpenNI ID.
 * 
 * Internally uses an optimized data structur for faster user lookup when receiving joint data.
 * 
 * @since 0.1
 */
public class FastUserManager implements UserManager {

	private static final Log LOG = LogFactory.getLog(FastUserManager.class);
	
	private static final int MAX_CONCURRENT_USERS = 50; // should be enough ;)

	private final UserFactory userFactory = new UserFactory(); // TODO inject dependency
	
	/** Internal data storage for users, optimized for OpenNI's ID generation algorithm (starting by 1, and reusing old IDs) .*/
	private final ArrayList<User> usersByOpenniId = new ArrayList<User>(MAX_CONCURRENT_USERS);
	{
		this.usersByOpenniId.add(null); // setup 0/1 base index conversation
	}
	

	public User lookupUser(int openniId/*is 1-base indexed*/, int userStateId) {
		LOG.debug("delegateOnUserMessage(openniId=" + openniId + ", userStateId=" + userStateId + ")");
		
		User returningUser = null;
		
		switch(userStateId) {
		
			case UserStateConstants.ID_NEW:
				returningUser = this.userFactory.create(openniId);
				if(this.usersByOpenniId./*1-base-*/size() > openniId) {
					User oldUser = this.usersByOpenniId.set(openniId - 1, returningUser);
					assert(oldUser == null);
				} else {
					this.usersByOpenniId.add(returningUser);
				}
				break;
				
			case UserStateConstants.ID_CALIBRATION_STARTED:
			case UserStateConstants.ID_CALIBRATION_FAILED:
			case UserStateConstants.ID_TRACKING:
				returningUser = this.usersByOpenniId.get(openniId);
				break;
				
			case UserStateConstants.ID_LOST:
				assert(this.usersByOpenniId.get(openniId) != null);
				returningUser = this.usersByOpenniId.set(openniId, null); // free memory
				break;
				
			default:
				throw new RuntimeException("Unhandled user state ID: " + userStateId + " (openniId was: " + openniId+ ")");
		}
		
		returningUser.setState(UserStateConstants.MAP_ID_TO_ENUM[userStateId]);
		return returningUser;
	}

	/**
	 * High performance access, as will be invoked everytime joint data was received!
	 * 
	 * @param openniId of the returned user
	 * @param callback ignored, as we assume a stable session (no OSC like stream) and want to be faaast
	 */
	public User getUser(int openniId) {
		return this.usersByOpenniId.get(openniId);
	}
	
}
