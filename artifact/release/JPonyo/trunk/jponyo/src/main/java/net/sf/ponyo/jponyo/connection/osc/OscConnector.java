package net.sf.ponyo.jponyo.connection.osc;

import java.net.SocketException;
import java.util.Date;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;

public class OscConnector implements Connector {
	
//	TODO private final int port;
	
	public Connection openConnection() {
		final OSCPortIn port;
		try {
			port = new OSCPortIn(7000);
		} catch (SocketException e) {
			throw new RuntimeException("Could not open oscport!", e);
		}
		
		final OscConnection connection = new OscConnection(port);
		port.addListener("/user_state", new OSCListener() {
			public void acceptMessage(Date timestamp, OSCMessage oscMessage) {
				connection.onOscUserMessage(oscMessage);
			}
		});
		port.addListener("/joint_data", new OSCListener() {
			public void acceptMessage(Date timestamp, OSCMessage oscMessage) {
				connection.onOscJointMessage(oscMessage);
			}
		});
		port.startListening();
		// TODO what for? ... port.run();
		return connection;
	}

}
