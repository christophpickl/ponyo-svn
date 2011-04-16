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
public class UserManager {

	private static final Log LOG = LogFactory.getLog(UserManager.class);
	
	private static final int USER_STATE_ID_NEW = 0;
	private static final int USER_STATE_ID_CALIBRATION_STARTED = 1;
	private static final int USER_STATE_ID_CALIBRATION_FAILED = 2;
	private static final int USER_STATE_ID_TRACKING = 3;
	private static final int USER_STATE_ID_LOST = 4;
	
	private static final UserState[] USER_STATE_ENUM_BY_ID = new UserState[5];
	static {
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_NEW] = UserState.NEW;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_CALIBRATION_STARTED] = UserState.CALIBRATION_STARTED;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_CALIBRATION_FAILED] = UserState.CALIBRATION_FAILED;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_TRACKING] = UserState.TRACKING;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_LOST] = UserState.LOST;
	}
	
	private static final int MAX_CONCURRENT_USERS = 50; // should be enough ;)

	private int userIdSequence = 0;
	
	/** Internal data storage for users, optimized for OpenNI's ID generation algorithm (starting by 1, and reusing old IDs) .*/
	private final ArrayList<User> usersByOpenniId = new ArrayList<User>(MAX_CONCURRENT_USERS);
	{
		this.usersByOpenniId.add(null); // setup 0/1 base index conversation
	}
	

	public User processUserMessage(int openniId/*is 1-base indexed*/, int userStateId) {
		LOG.debug("processUserMessage(openniId=" + openniId + ", userStateId=" + userStateId + ")");
		
		User returningUser = null;
		
		switch(userStateId) {
		
			case USER_STATE_ID_NEW:
				returningUser = new User(this.userIdSequence++, openniId, 0xFF0000);
				if(this.usersByOpenniId./*1-base-*/size() > openniId) {
					User oldUser = this.usersByOpenniId.set(openniId - 1, returningUser);
					assert(oldUser == null);
				} else {
					this.usersByOpenniId.add(returningUser);
				}
				break;
				
			case USER_STATE_ID_CALIBRATION_STARTED:
			case USER_STATE_ID_CALIBRATION_FAILED:
			case USER_STATE_ID_TRACKING:
				returningUser = this.usersByOpenniId.get(openniId);
				break;
				
			case USER_STATE_ID_LOST:
				assert(this.usersByOpenniId.get(openniId) != null);
				returningUser = this.usersByOpenniId.set(openniId, null); // free memory
				break;
				
			default:
				throw new RuntimeException("Unhandled user state ID: " + userStateId + " (openniId was: " + openniId+ ")");
		}
		
		returningUser.setState(USER_STATE_ENUM_BY_ID[userStateId]);
		return returningUser;
	}

	/**
	 * High performance access, as will be invoked everytime joint data was received!
	 */
	public User getUser(int openniId) {
		return this.usersByOpenniId.get(openniId);
	}
}
