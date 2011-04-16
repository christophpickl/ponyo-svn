package net.sf.ponyo.jponyo.global;

import net.sf.ponyo.jponyo.DevelopmentConstants;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnector;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserManager;
import net.sf.ponyo.jponyo.user.UserState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 0.1
 */
public class PonyoContext implements ConnectionListener {
	
	private static final Log LOG = LogFactory.getLog(PonyoContext.class);

	
	private final UserManager userManager = new UserManager();
	
	private final GlobalSpace space = new GlobalSpace();
	
	private final DefaultAsync<UserChangeListener> userChangeListeners = new DefaultAsync<UserChangeListener>();
	
	
	private Connection connection;
	
	static {
		boolean assertEnabled = false;
		assert (assertEnabled = true);
		if(assertEnabled) {
			System.out.println("Assertions are enabled, gooood ;)");
		} else {
			System.err.println("Oh No! Assertions are DISABLED!");
		}
	}
	
	public static void main(String[] args) {
		new PonyoContext().startOniRecording(DevelopmentConstants.ONI_PATH);
//		new PonyoContext().startXmlConfig(Constants.XML_PATH);
	}

	public void startXmlConfig(String xmlConfigPath) {
		this.internalStart(new JnaByConfigConnector(xmlConfigPath));
	}

	public void startOniRecording(String oniRecordingPath) {
		this.internalStart(new JnaByRecordingConnector(oniRecordingPath));
	}

	public void startOscReceiver() {
		this.internalStart(new OscConnector(/*TODO add startup params*/));
	}
	
	private void internalStart(Connector<? extends Connection> connector) {
		LOG.debug("internalStart(..) START");
		this.connection = connector.openConnection();
		this.connection.addListener(this);
		LOG.debug("internalStart(..) END");
	}
	// MINOR programatically enable assertions: ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);

	public GlobalSpace getGlobalSpace() {
		return this.space;
	}
	
	public void shutdown() {
		if(this.connection == null) {
			LOG.warn("Tried to shutdown but there is no connection to close!");
			return;
		}
		this.connection.close();
		this.connection = null;
	}
	
	private int i = 0;
	public void onJointMessage(int openniId, int jointId, float x, float y, float z) {
		if(this.i++ == 100) {
			this.i = 0;
			LOG.trace("joint for user with OpenNI id: " + openniId);
		}
		
		this.userManager.getUser(openniId).getSkeleton().updateJoint(jointId, x, y, z);
	}
	
	public void onUserMessage(int openniId/*is 1-base indexed*/, int userStateId) {
		LOG.debug("onUserMessage(openniId=" + openniId + ", userStateId=" + userStateId + ")");
		
		User user = this.userManager.processUserMessage(openniId, userStateId);
		
		if(user.getState() == UserState.NEW) {
			this.space.addUser(user);
		}
		
		for(UserChangeListener listener : this.userChangeListeners.getListeners()) {
			listener.onUserChanged(user, user.getState());
		}
		
		if(user.getState() == UserState.LOST) {
			this.space.removeUser(user);
		}
		
	}

	public void addUserChangeListener(UserChangeListener listener) {
		this.userChangeListeners.addListener(listener);
	}
	
	public void removeUserChangeListener(UserChangeListener listener) {
		this.userChangeListeners.removeListener(listener);
	}
	
}
