package net.pulseproject.mkinector.debugapp.misc;

import java.util.HashMap;
import java.util.Map;

import net.pulseproject.mkinector.debugapp.view.UserPanel;
import net.pulseproject.mkinector.debugapp.view.UserPanelFactory;
import net.pulseproject.mkinector.josceleton.api.JosceletonConnectionListener;
import net.pulseproject.mkinector.josceleton.api.message.JointMessage;
import net.pulseproject.mkinector.josceleton.api.message.UserMessage;
import net.pulseproject.mkinector.josceleton.api.user.User;
import net.pulseproject.mkinector.josceleton.api.user.UserManagerListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.assistedinject.Assisted;

public class OscConnectionWindowGlue implements UserManagerListener, JosceletonConnectionListener {

	private static final Log LOG = LogFactory.getLog(OscConnectionWindowGlue.class);
	

	private final UserPanelFactory userPanelFactory;
	
	private final Map<User, UserPanel> userPanels = new HashMap<User, UserPanel>();

	private final OscConnectionWindowGlueListener listener;

	private int userWaitingCount = 0;
	
	private int userReadyCount = 0;
	
	
	public OscConnectionWindowGlue(
			final UserPanelFactory userPanelFactory,
			@Assisted final OscConnectionWindowGlueListener listener) {
		this.userPanelFactory = userPanelFactory;
		if(listener == null) { throw new IllegalArgumentException("listener == null"); }
		this.listener = listener;
	}
	
	@Override public final void onJointMessage(final JointMessage message) {
		final UserPanel panel = this.userPanels.get(message.getUser());
		panel.updateCoordinate(message.getBodyPart(), message.getCoordinate());
	}

	@Override public final void onUserMessage(final UserMessage message) {
		// ignore
	}

	@Override public final void onUserDead(final User user) {
		LOG.info("onUserDead(user=" + user + ")");
		
		final UserPanel userPanel = this.userPanels.remove(user);
		if(userPanel == null) {
			throw new IllegalArgumentException("No registered UserPanel for user [" + user + "]!");
		}
		this.listener.onRemoveUserPanel(userPanel);
		
		
		if(userPanel.isSkeletonAvailable() == true) {
			this.userReadyCount--;
		} else {
			this.userWaitingCount--;
		}
		this.listener.onUserCountChanged(this.userReadyCount, this.userWaitingCount);
	}

	@Override public final void onUserProcessing(final User user) {
		LOG.info("onUserProcessing(user=" + user + ")");
		
		if(this.userPanels.containsKey(user) == false) {
			this.doAddUser(user);
		}
		
		final UserPanel userPanel = this.userPanels.get(user);
		userPanel.setSkeletonAvailableTrue();
		
		this.userWaitingCount--;
		this.userReadyCount++;
		this.listener.onUserCountChanged(this.userReadyCount, this.userWaitingCount);
	}

	@Override public final void onUserWaiting(final User user) {
		LOG.info("onUserWaiting(user=" + user + ")");
		
		this.doAddUser(user);
		this.listener.onUserCountChanged(this.userReadyCount, this.userWaitingCount);
	}

	private void doAddUser(final User user) {
		LOG.debug("doAddUser(user=" + user + ")");
		
		final UserPanel userPanel = this.userPanelFactory.create(user);
		this.userPanels.put(user, userPanel);
		this.listener.onAddUserPanel(userPanel);
		
		this.userWaitingCount++;
	}
	
//	public final void initWithUsers(final UserCollection users) {
//		for(final User user : users.getWaitingUsers()) {
//			this.onUserMessageTypedNewSkel(user);
//		}
//		for(final User user : users.getAvailableUsers()) {
//			this.onUserMessageTypedNewUser(user);
//		}
//	}
	
}
