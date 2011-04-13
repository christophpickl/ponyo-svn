package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.DefaultAsync;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnJointPositionChangedCallback;
import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnUserStateChangedCallback;

public class JnaConnection
	extends DefaultAsync<ConnectionListener>
		implements Connection, OnUserStateChangedCallback, OnJointPositionChangedCallback {
	
	private final String configPath;
	private JnaGate gate;
	
	public JnaConnection(String configPath) {
		this.configPath = configPath;
	}
	
	public Connection open() {
		this.gate = new JnaGate(this, this);
		this.gate.startWithXml(this.configPath);
		return this;
	}

	public void close() {
		if(this.gate == null) {
			throw new IllegalStateException();
		}
		this.gate.destroy();
	}

	public void onUserStateChanged(int userId, int userState) {
		for(ConnectionListener listener : this.getListeners()) {
			listener.onUserMessage(userId, userState);
		}
	}

	public void onJointPositionChanged(int userId, int jointId, float x, float y, float z) {
		for(ConnectionListener listener : this.getListeners()) {
			listener.onJointMessage(userId, jointId, x, y, z);
		}
	}
}
