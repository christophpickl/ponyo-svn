package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.Listener;

/**
 * @since 0.1
 */
public interface ConnectionListener extends Listener {

	/**
	 * @since 0.1
	 */
	void onJointMessage(int userId, int/*TODO change to byte?!*/ jointId, float x, float y, float z);

	/**
	 * @since 0.1
	 */
	void onUserMessage(int userId, int userState);
	
}
