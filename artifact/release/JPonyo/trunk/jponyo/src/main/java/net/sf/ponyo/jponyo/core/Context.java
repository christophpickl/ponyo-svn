package net.sf.ponyo.jponyo.core;

import java.util.concurrent.LinkedBlockingQueue;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.JointData;
import net.sf.ponyo.jponyo.connection.JointMessage;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.stream.ContinuousMotionStream;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStream;
import net.sf.ponyo.jponyo.stream.MotionStreamImpl;
import net.sf.ponyo.jponyo.user.ContinuousUserProvider;
import net.sf.ponyo.jponyo.user.ContinuousUserProviderImpl;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserManager;
import net.sf.ponyo.jponyo.user.UserManagerCallback;
import net.sf.ponyo.jponyo.user.UserState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 0.1
 */
public class Context implements ConnectionListener, UserManagerCallback {
	
	private static final Log LOG = LogFactory.getLog(Context.class);
	
	private final DefaultAsync<UserChangeListener> userChangeListeners = new DefaultAsync<UserChangeListener>();
	private final Connection connection;
	private /*TODO lazy set*/ UserManager userManager;
	private final LinkedBlockingQueue<MotionData> jointQueue = new LinkedBlockingQueue<MotionData>();
	private final GlobalSpace space = new GlobalSpace();
	
	private final DispatchThread dispatcher = new DispatchThread(this.jointQueue);
	private final MotionStreamImpl motionStream = new MotionStreamImpl();
	private ContinuousMotionStream continuousMotionStream;
	private ContinuousUserProvider continuousUserProvider;
	
	Context(Connection connection) {
		this.connection = connection;
	}

	void start() {
		this.dispatcher.addListener(this.motionStream);
		this.dispatcher.start();
	}
	
	private int i = 0;

	public void onJointMessage(JointMessage message) {
		if(this.i++ == 1000) {
			this.i = 0;
			LOG.trace("onJointMessage(" + message + ")");
		}
		
		if(this.i % 100 == 0) {
			if(this.jointQueue.size() > 50) {
				LOG.warn("this.jointQueue: size=" + this.jointQueue.size());
			}
		}
		
		User user = this.userManager.lookupForJointMessage(message.getUserId());
		user.getSkeleton().update(message.getData());
		
		JointData jointData = message.getData();
		this.jointQueue.add(new MotionData(user, Joint.byId(jointData.getJointId()), jointData.getJointPosition()));
	}

	public void processUserStateChange(User user, UserState state) {
		
		if(state == UserState.NEW) {
			this.space.addUser(user);
		}
//		System.out.println("broadcasting to: " + this.userChangeListeners.getListeners());
		for(UserChangeListener listener : this.userChangeListeners.getListeners()) {
			listener.onUserChanged(user, state);
		}
		if(state == UserState.LOST) {
			this.space.removeUser(user);
		}
	}
	
	public void onUserMessage(int openniId/*is 1-base indexed*/, int userStateId) {
		User user = this.userManager.lookupForUserMessage(openniId, userStateId);
		this.processUserStateChange(user, user.getState());
	}
	
	public void shutdown() {
		if(this.connection == null) {
			LOG.warn("Tried to shutdown but there is no connection to close!");
			return;
		}
		
		this.dispatcher.removeListener(this.motionStream);

		if(this.continuousMotionStream != null) {
			this.dispatcher.removeListener(this.continuousMotionStream);
			this.userChangeListeners.removeListener(this.continuousMotionStream);
		}
		if(this.continuousUserProvider != null) {
			this.userChangeListeners.removeListener(this.continuousUserProvider);
		}
		
		try {
			LOG.trace("Waiting for dispatch thread to die.");
			this.dispatcher.stop();
		} catch (InterruptedException e) {
			throw new RuntimeException("Could not stop dispatcher thread!", e);
		}
		
		this.connection.close();
	}
	
	
	public ContinuousMotionStream getContinuousMotionStream() {
		if(this.continuousMotionStream == null) {
			this.initializeContinuousMotionStream();
		}
		return this.continuousMotionStream;
	}

	public ContinuousUserProvider getContinuousUserProvider() {
		if(this.continuousUserProvider == null) {
			this.initializeContinuousUserProvider();
		}
		return this.continuousUserProvider;
	}
	
	private void initializeContinuousMotionStream() {
		this.continuousMotionStream = new ContinuousMotionStream(this.motionStream, this.space);
		this.continuousMotionStream.initAttachingToUser();
		
		this.userChangeListeners.addListener(this.continuousMotionStream);
		this.dispatcher.addListener(this.continuousMotionStream);
	}
	
	private void initializeContinuousUserProvider() {
		LOG.debug("Lazy initializing initializeContinuousUserProvider.");
		
		this.continuousUserProvider = new ContinuousUserProviderImpl(this.space);
		this.continuousUserProvider.init();
		
		this.userChangeListeners.addListener(this.continuousUserProvider);
	}
	
	public MotionStream getMotionStream() {
		return this.motionStream;
	}

	public void addUserChangeListener(UserChangeListener listener) {
		this.userChangeListeners.addListener(listener);
	}
	
	public void removeUserChangeListener(UserChangeListener listener) {
		this.userChangeListeners.removeListener(listener);
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public GlobalSpace getGlobalSpace() {
		return this.space;
	}
	
}
