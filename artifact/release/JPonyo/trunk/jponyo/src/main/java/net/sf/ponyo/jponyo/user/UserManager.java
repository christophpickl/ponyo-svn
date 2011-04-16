package net.sf.ponyo.jponyo.user;

import net.sf.ponyo.jponyo.connection.ConnectionListener;


/**
 * @since 0.1
 */
public interface UserManager {

	/**
	 * Actually just an onUserMessage received delegate.
	 * 
	 * @see ConnectionListener#onUserMessage(int, int)
	 * @since 0.1
	 */
	User lookupUser(int openniId/*is 1-base indexed*/, int userStateId);

	/**
	 * Will be invoked on every received joint data, as we need to get the {@link User} instance by given ID.
	 * 
	 * Actually just an onJointMessage received delegate.
	 * 
	 * @param openniId user ID
	 * @return user for given ID.
	 * @see ConnectionListener#onJointMessage(int, int, float, float, float)
	 * @since 0.1
	 */
	User getUser(int openniId);

}