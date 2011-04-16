package net.sf.ponyo.jponyo.user;

import net.sf.ponyo.jponyo.common.async.Listener;

/**
 * @since 0.1
 */
public interface UserChangeListener extends Listener {

	void onUserChanged(User user, UserState state);
	
}
