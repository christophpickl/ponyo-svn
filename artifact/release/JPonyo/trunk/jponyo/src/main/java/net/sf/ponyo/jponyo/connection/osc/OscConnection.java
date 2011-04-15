package net.sf.ponyo.jponyo.connection.osc;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;

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
		final Object[] messageArgs = oscMessage.getArguments();
		
		if(messageArgs.length != 1) {
//			TODO throw InvalidArgumentException.newInstance("oscMessage.arguments.length", messageArgs.length, "==1");
		}
		
		final Integer userId = (Integer) messageArgs[0];
		final Integer stateId = (Integer) messageArgs[1];
		
		for(final ConnectionListener listener : this.getListeners()) {
			listener.onUserMessage(userId.intValue(), stateId.intValue());
		}
	}

	public void onOscJointMessage(OSCMessage oscMessage) {
		final Object[] messageArgs = oscMessage.getArguments();

		if(messageArgs.length != 5) {
//			TODO throw InvalidArgumentException.newInstance("oscMessage.arguments.length", messageArgs.length, "==5");
		}
		
		final Integer userId = (Integer) messageArgs[0];
		final Integer jointId = (Integer) messageArgs[1];
		
		final float x = ((Float) messageArgs[2]).floatValue();
		final float y = ((Float) messageArgs[3]).floatValue();
		final float z = ((Float) messageArgs[4]).floatValue();
		
		for(final ConnectionListener listener : this.getListeners()) {
			listener.onJointMessage(userId.intValue(), jointId.intValue(), x, y, z);
		}
	}

}
