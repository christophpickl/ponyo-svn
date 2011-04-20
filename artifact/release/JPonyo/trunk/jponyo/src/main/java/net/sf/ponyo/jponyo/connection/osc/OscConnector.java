package net.sf.ponyo.jponyo.connection.osc;

import java.net.SocketException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.user.RunningSessionAwareUserManager;
import net.sf.ponyo.jponyo.user.UserManager;
import net.sf.ponyo.jponyo.user.UserManagerCallback;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class OscConnector implements Connector<OscConnection> {
	
	private static final Log LOG = LogFactory.getLog(OscConnector.class);
	
	private static final String OSC_ADDRESS_USER_STATE = "/user_state";
	private static final String OSC_ADDRESS_JOINT_DATA = "/joint_data";
	private static final int DEFAULT_PORT = 7000;
//	TODO private final int port;
	
	public OscConnection openConnection() {
		LOG.debug("Opening connectoin to default port " + DEFAULT_PORT + " ...");
		final OSCPortIn port;
		try {
			port = new OSCPortIn(DEFAULT_PORT);
		} catch (SocketException e) {
			throw new RuntimeException("Could not open oscport!", e);
		}
		
		final OscConnection connection = new OscConnection(port);
		port.addListener(OSC_ADDRESS_USER_STATE, new OSCListener() {
			public void acceptMessage(Date timestamp, OSCMessage oscMessage) {
				connection.onOscUserMessage(oscMessage);
			}
		});
		port.addListener(OSC_ADDRESS_JOINT_DATA, new OSCListener() {
			public void acceptMessage(Date timestamp, OSCMessage oscMessage) {
				connection.onOscJointMessage(oscMessage);
			}
		});
		port.startListening();
		// TODO what for? ... port.run();
		return connection;
	}

	public UserManager createUserManager(UserManagerCallback callback) {
		return new RunningSessionAwareUserManager(callback);
	}

}
