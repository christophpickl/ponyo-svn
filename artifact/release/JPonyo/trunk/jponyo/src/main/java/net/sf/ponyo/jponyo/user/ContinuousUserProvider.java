package net.sf.ponyo.jponyo.user;

import net.sf.ponyo.jponyo.common.async.Async;

public interface ContinuousUserProvider extends UserChangeListener, Async<ContinuousUserListener> {

	void init();
	
	User getCurrentUser();

}
