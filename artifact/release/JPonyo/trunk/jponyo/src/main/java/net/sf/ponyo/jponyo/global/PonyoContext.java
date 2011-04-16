package net.sf.ponyo.jponyo.global;

import java.util.ArrayList;

import net.sf.ponyo.jponyo.Constants;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnector;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;
import net.sf.ponyo.jponyo.entity.User;
import net.sf.ponyo.jponyo.entity.UserState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PonyoContext implements ConnectionListener {
	
	private static final Log LOG = LogFactory.getLog(PonyoContext.class);

	private static final int USER_STATE_ID_NEW = 0;
	private static final int USER_STATE_ID_CALIBRATION_STARTED = 1;
	private static final int USER_STATE_ID_CALIBRATION_FAILED = 2;
	private static final int USER_STATE_ID_TRACKING = 3;
	private static final int USER_STATE_ID_LOST = 4;
	private static final UserState[] USER_STATE_ENUM_BY_ID = new UserState[5];
	static {
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_NEW] = UserState.NEW;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_CALIBRATION_STARTED] = UserState.CALIBRATION_STARTED;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_CALIBRATION_FAILED] = UserState.CALIBRATION_FAILED;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_TRACKING] = UserState.TRACKING;
		USER_STATE_ENUM_BY_ID[USER_STATE_ID_LOST] = UserState.LOST;
	}
	private static int userIdSequence = 0;
	private static final int MAX_CONCURRENT_USERS = 20; // should be enough ;)
	
	/** optimized for direct access via OpenNI custom id (starting by 1, and reusing old ids) */
	private final ArrayList<User> usersByOpenniId = new ArrayList<User>(MAX_CONCURRENT_USERS);
	{
		this.usersByOpenniId.add(null); // setup 0/1 base index conversation
	}
	
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
		new PonyoContext().startOniRecording(Constants.ONI_PATH);
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
		this.connection = connector.openConnection();
		this.connection.addListener(this);
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
	public void onJointMessage(int userId, int jointId, float x, float y, float z) {
		if(this.i++ == 100) { LOG.trace("joint for user " + userId); }
		this.usersByOpenniId.get(userId).getSkeleton().updateJoint(jointId, x, y, z);
	}
	
	public void onUserMessage(int openniId/*is 1-base indexed*/, int userStateId) {
		LOG.debug("onUserMessage(userId=" + openniId + ", userStateId=" + userStateId + ")");
		if(true) {
			return;
		}
		
		LOG.debug("  USER_STATE_ENUM_BY_ID[userStateId]=" + USER_STATE_ENUM_BY_ID[userStateId]);
		
		User user = null;
		switch(userStateId) {
			case USER_STATE_ID_NEW:
				user = new User(userIdSequence++, openniId, 0xFF0000);
				this.space.addUser(user);
				if(this.usersByOpenniId./*1-base-*/size() > openniId) {
					User oldUser = this.usersByOpenniId.set(openniId - 1, user);
					assert(oldUser == null);
				} else {
					this.usersByOpenniId.add(user);
				}
				break;
			case USER_STATE_ID_CALIBRATION_STARTED:
			case USER_STATE_ID_CALIBRATION_FAILED:
			case USER_STATE_ID_TRACKING:
			case USER_STATE_ID_LOST:
				user = this.usersByOpenniId.get(openniId);
				break;
			default: assert(false);
		}
		assert(user != null);
		
		user.setState(USER_STATE_ENUM_BY_ID[userStateId]);
		for(UserChangeListener listener : this.userChangeListeners.getListeners()) {
			switch(userStateId) {
				case USER_STATE_ID_NEW:
					listener.onUserNew(user);
					break;
				case USER_STATE_ID_CALIBRATION_STARTED:
					listener.onUserCalibrationStarted(user);
					break;
				case USER_STATE_ID_CALIBRATION_FAILED:
					listener.onUserCalibrationFailed(user);
					break;
				case USER_STATE_ID_TRACKING:
					listener.onUserTracking(user);
					break;
				case USER_STATE_ID_LOST:
					listener.onUserLost(user);
					break;
				default: assert(false);
			}
		}
		
		if(userStateId == USER_STATE_ID_LOST) {
			User deletedUser = this.usersByOpenniId.set(user.getOpenniId(), null); // free memory
			this.space.removeUser(deletedUser);
			assert(deletedUser != null);
		}
	}

	public void addUserChangeListener(UserChangeListener listener) {
		this.userChangeListeners.addListener(listener);
	}
	
	public void removeUserChangeListener(UserChangeListener listener) {
		this.userChangeListeners.removeListener(listener);
	}
	
}
