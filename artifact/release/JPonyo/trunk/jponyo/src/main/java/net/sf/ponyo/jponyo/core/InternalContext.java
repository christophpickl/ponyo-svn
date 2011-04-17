package net.sf.ponyo.jponyo.core;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserManagerCallback;
import net.sf.ponyo.jponyo.user.UserState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class InternalContext extends DefaultAsync<UserChangeListener> implements UserManagerCallback {

	private static final Log LOG = LogFactory.getLog(InternalContext.class);

	private final GlobalSpace space = new GlobalSpace();
	
	public void processUserStateChange(User user, UserState state) {
		LOG.debug("processUserStateChange(user="+user+", state="+state+")");
		
		if(state == UserState.NEW) {
			this.space.addUser(user);
		}
		for(UserChangeListener listener : this.getListeners()) {
			listener.onUserChanged(user, state);
		}
		if(state == UserState.LOST) {
			this.space.removeUser(user);
		}
	}

	public GlobalSpace getGlobalSpace() {
		return this.space;
	}
}
