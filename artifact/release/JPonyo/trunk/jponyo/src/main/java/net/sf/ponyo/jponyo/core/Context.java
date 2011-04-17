package net.sf.ponyo.jponyo.core;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 0.1
 */
public class Context implements ConnectionListener {
	
	private static final Log LOG = LogFactory.getLog(Context.class);

	// TODO write state aspect (if started, not may start again)
	
	private final Connection connection;
	private final InternalContext iContext;
	private final UserManager userManager;
	
	static {
		boolean assertEnabled = false;
		assert (assertEnabled = true);
		if(assertEnabled) {
			System.out.println("Assertions are enabled, gooood ;)");
		} else {
			System.err.println("Oh No! Assertions are DISABLED!");
		}
	}
	
	public Context(Connection connection, InternalContext iContext, UserManager userManager) {
		this.connection = connection;
		this.iContext = iContext;
		this.userManager = userManager;
	}

	public static void main(String[] args) {
		new ContextStarter().startOniRecording(DevelopmentConstants.ONI_PATH);
//		new PonyoContext().startXmlConfig(Constants.XML_PATH);
	}

	// MINOR programatically enable assertions: ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);

	public GlobalSpace getGlobalSpace() {
		return this.iContext.getGlobalSpace();
	}
	
	public void shutdown() {
		if(this.connection == null) {
			LOG.warn("Tried to shutdown but there is no connection to close!");
			return;
		}
		this.connection.close();
	}
	
	private int i = 0;
	public void onJointMessage(int openniId, int jointId, float x, float y, float z) {
		if(this.i++ == 500) {
			this.i = 0;
			LOG.trace("joint for user with OpenNI id: " + openniId + ", joint: " + jointId + ", x: " + x);
		}
		
		this.userManager.lookupForJointMessage(openniId).getSkeleton().updateJoint(jointId, x, y, z);
	}
	
	public void onUserMessage(int openniId/*is 1-base indexed*/, int userStateId) {
		LOG.debug("onUserMessage(openniId=" + openniId + ", userStateId=" + userStateId + ")");
		
		User user = this.userManager.lookupForUserMessage(openniId, userStateId);
		this.iContext.processUserStateChange(user, user.getState());
	}

	public void addUserChangeListener(UserChangeListener listener) {
		this.iContext.addListener(listener);
	}
	
	public void removeUserChangeListener(UserChangeListener listener) {
		this.iContext.removeListener(listener);
	}
	
}
