package net.sf.ponyo.jponyo.connection.osc;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.common.math.Array3f;
import net.sf.ponyo.jponyo.connection.AdvancedCapabilities;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.JointData;
import net.sf.ponyo.jponyo.connection.JointMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

class OscConnection extends DefaultAsync<ConnectionListener> implements Connection {
	
	private static final Log LOG = LogFactory.getLog(OscConnection.class);
	
	private final OSCPortIn port;
	
	public OscConnection(OSCPortIn port) {
		this.port = port;
	}

	@Override
	protected synchronized void beforeAddListener(ConnectionListener listener) {
		if(this.port.isListening() == false) {
			throw new IllegalStateException("already closed!");
		}
	}
	
	public void close() {
		if(this.port.isListening() == false) {
			LOG.warn("Not closing OSC port, as it is not set to listening state!");
			return;
		}
		
		this.port.stopListening();
		this.port.close();
	}
	
	public void onOscUserMessage(OSCMessage oscMessage) {
		Object[] messageArgs = oscMessage.getArguments();
		
		if(messageArgs.length != 1) {
//			TODO throw InvalidArgumentException.newInstance("oscMessage.arguments.length", messageArgs.length, "==1");
		}
		
		Integer userId = (Integer) messageArgs[0];
		Integer stateId = (Integer) messageArgs[1];
		
		for(ConnectionListener listener : this.getListeners()) {
			listener.onUserMessage(userId.intValue(), stateId.intValue());
		}
	}

	public void onOscJointMessage(OSCMessage oscMessage) {
		final Object[] messageArgs = oscMessage.getArguments();

		if(messageArgs.length != 5) {
//			TODO throw InvalidArgumentException.newInstance("oscMessage.arguments.length", messageArgs.length, "==5");
		}
		
		Integer userId = (Integer) messageArgs[0];
		Integer jointId = (Integer) messageArgs[1];
		
		float x = ((Float) messageArgs[2]).floatValue();
		float y = ((Float) messageArgs[3]).floatValue();
		float z = ((Float) messageArgs[4]).floatValue();
		
		JointMessage message = new JointMessage(userId.intValue(), new JointData(jointId.intValue(), new Array3f(x, y, z)));
		for(final ConnectionListener listener : this.getListeners()) {
			listener.onJointMessage(message);
		}
	}

	public AdvancedCapabilities getAdvancedCapabilities() {
		throw new UnsupportedOperationException("An OSC connection has no advanced capabilities!");
	}

	public boolean hasAdvancedCapabilities() {
		return false;
	}

}
