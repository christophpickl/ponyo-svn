package net.sf.ponyo.jponyo.stream;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.core.GlobalSpace;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserState;

/**
 * @since 0.1
 */
public class ContinuousMotionStream
	extends DefaultAsync<MotionStreamListener>
		implements MotionStreamListener, UserChangeListener {
	
	private static final Log LOG = LogFactory.getLog(ContinuousMotionStream.class);
	
	private final MotionStream internalStream;
	private final GlobalSpace space;
	
	private User currentlyTrackedUser;
	
	public ContinuousMotionStream(MotionStream internalStream, GlobalSpace space) {
		this.internalStream = internalStream;
		this.space = space;
	}
	
	public void initAttachingToUser() {
		final Collection<User> trackedUsers = this.space.getFilteredUsers(UserState.TRACKING);
		LOG.debug("initAttachingToUser() ... trackedUsers.size=" + trackedUsers.size());
		
		if(trackedUsers.isEmpty() == false) {
			// return next == first == oldest user to reattach as our next victim, wuhahaha ;)
			this.startListeningTo(trackedUsers.iterator().next());
		}
	}

	public void onMotion(MotionData data) {
//		LOG.info("onMotion(data="+data+") ... this.currentlyTrackedUser=" + this.currentlyTrackedUser);
		if(this.currentlyTrackedUser != null && this.currentlyTrackedUser == data.getUser()) {
			for (MotionStreamListener listener : this.getListeners()) {
				listener.onMotion(data);
			}
		}
	}

	public void onUserChanged(User user, UserState state) {
		if(state == UserState.TRACKING && this.currentlyTrackedUser == null) {
			this.startListeningTo(user);
		} else if(state == UserState.LOST && this.currentlyTrackedUser == user) {
			this.stopListeningTo();
		}
	}
	
	private void startListeningTo(User user) {
		LOG.debug("startListeningTo(user=" + user + ")");
		
		this.currentlyTrackedUser = user;
		this.internalStream.addListenerFor(this.currentlyTrackedUser, this);
	}
	
	private void stopListeningTo() {
		LOG.debug("stopListeningTo()");
		
		this.internalStream.removeListenerFor(this.currentlyTrackedUser, this);
		this.currentlyTrackedUser = null;
		
		this.initAttachingToUser(); // retry
	}

}
