package net.sf.ponyo.jponyo.connection.osc;

import java.net.SocketException;
import java.util.Date;

import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.user.RunningSessionAwareUserManager;
import net.sf.ponyo.jponyo.user.UserManager;
import net.sf.ponyo.jponyo.user.UserManagerCallback;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class OscConnector implements Connector<OscConnection> {
	
	private static final String OSC_ADDRESS_USER_STATE = "/user_state";
	private static final String OSC_ADDRESS_JOINT_DATA = "/joint_data";
//	TODO private final int port;
	
	private final UserManagerCallback callback;
	
	public OscConnector(UserManagerCallback callback) {
		this.callback = callback;
	}
	
	public OscConnection openConnection() {
		final OSCPortIn port;
		try {
			port = new OSCPortIn(7000);
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

	public UserManager createUserManager() {
		return new RunningSessionAwareUserManager(this.callback);
	}

}
