package net.sf.ponyo.jponyo.user;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.core.GlobalSpace;

// TODO somehow this is duplicate of ContinuousMotionStream (CMS could use this UserProvider...)
public class ContinuousUserProviderImpl extends DefaultAsync<ContinuousUserListener> implements ContinuousUserProvider {

	private static final Log LOG = LogFactory.getLog(ContinuousUserProviderImpl.class);
	
	private final GlobalSpace space;
	private User currentUser;
	
	public ContinuousUserProviderImpl(GlobalSpace space) {
		LOG.debug("new ContinuousUserProviderImpl()");
		this.space = space;
	}

	public void init() {
		this.checkUsers();
	}

	public void onUserChanged(User user, UserState state) {
		LOG.debug("onUserChanged(user="+user+", state="+state+")");
		
		if(this.currentUser == null && state == UserState.TRACKING) {
			this.currentUser = user;
			this.dispatchCurrentUserChanged();
			
		} else if(this.currentUser == user && state == UserState.LOST) {
			this.checkUsers();
		}
	}
	
	private void checkUsers() {
		Collection<User> filteredUsers = this.space.getFilteredUsers(UserState.TRACKING);
		if(filteredUsers.isEmpty() == true) {
			this.currentUser = null;
			this.dispatchCurrentUserChanged();
			return;
		}
		
		User newUser = filteredUsers.iterator().next();
		this.currentUser = newUser;
		this.dispatchCurrentUserChanged();
	}
	
	private void dispatchCurrentUserChanged() {
		LOG.debug("dispatchCurrentUserChanged() ... this.currentUser=" + this.currentUser);
		for (ContinuousUserListener listener : this.getListeners()) {
			listener.onCurrentUserChanged(this.currentUser);
		}
	}

	public User getCurrentUser() {
		return this.currentUser;
	}

}
