package net.sf.ponyo.jponyo.user;

import net.sf.ponyo.jponyo.common.async.Listener;

public interface ContinuousUserListener extends Listener {
	
	/**
	 * @param user can be null.
	 */
	void onCurrentUserChanged(User user);
	
}
