package net.sf.ponyo.jponyo.connection;


/**
 * @since 0.1
 */
public interface ConnectionListener extends ConnectionJointListener {

	/**
	 * @since 0.1
	 */
	void onUserMessage(int userId, int userState);
	
}
