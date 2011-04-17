package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.common.async.Listener;

/**
 * @since 0.1
 */
public interface ConnectionJointListener extends Listener {

	/**
	 * @since 0.1
	 */
	void onJointMessage(JointMessage message);
	
}
