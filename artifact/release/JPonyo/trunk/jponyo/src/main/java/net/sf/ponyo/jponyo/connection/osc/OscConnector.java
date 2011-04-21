package net.sf.ponyo.jponyo.connection.osc;

import java.net.SocketException;
import java.util.Date;

import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.user.RunningSessionUserManagerFactory;
import net.sf.ponyo.jponyo.user.UserManager;
import net.sf.ponyo.jponyo.user.UserManagerCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class OscConnector implements Connector<OscConnection> {
	
	public static final int DEFAULT_PORT = 7000;
	
	private static final Log LOG = LogFactory.getLog(OscConnector.class);
	private static final String OSC_ADDRESS_USER_STATE = "/user_state";
	private static final String OSC_ADDRESS_JOINT_DATA = "/joint_data";
	
	private final RunningSessionUserManagerFactory factory;
	private final int port;
	
	@Inject
	public OscConnector(RunningSessionUserManagerFactory factory, @Assisted int port) {
		LOG.debug("new OscConnector(.., port=" + port + ")");
		this.factory = factory;
		this.port = port;
	}
	
	public OscConnection openConnection() {
		final OSCPortIn oscPortIn;
		try {
			LOG.debug("Opening connection on port " + this.port + " ...");
			oscPortIn = new OSCPortIn(this.port);
		} catch (SocketException e) {
			throw new RuntimeException("Could not open connection on OSC port " + this.port + "!", e);
		}
		
		final OscConnection connection = new OscConnection(oscPortIn);
		oscPortIn.addListener(OSC_ADDRESS_USER_STATE, new OSCListener() {
			public void acceptMessage(Date timestamp, OSCMessage oscMessage) {
				connection.onOscUserMessage(oscMessage);
			}
		});
		oscPortIn.addListener(OSC_ADDRESS_JOINT_DATA, new OSCListener() {
			public void acceptMessage(Date timestamp, OSCMessage oscMessage) {
				connection.onOscJointMessage(oscMessage);
			}
		});
		oscPortIn.startListening();
		// TODO what for? ... port.run();
		return connection;
	}

	public UserManager createUserManager(UserManagerCallback callback) {
		return this.factory.create(callback);
	}

}
