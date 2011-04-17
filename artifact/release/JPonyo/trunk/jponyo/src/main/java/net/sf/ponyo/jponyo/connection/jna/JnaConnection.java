package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.common.data.Array3f;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.JointData;
import net.sf.ponyo.jponyo.connection.JointMessage;
import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnJointPositionChangedCallback;
import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnUserStateChangedCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JnaConnection
	extends DefaultAsync<ConnectionListener>
		implements Connection, OnUserStateChangedCallback, OnJointPositionChangedCallback {
	
	private static final Log LOG = LogFactory.getLog(JnaConnection.class);
	
	private final String configOrRecordingPath;
	
	private PnJNALibraryWrapper jnaLib;
	
	public JnaConnection(String configOrRecordingPath) {
		this.configOrRecordingPath = configOrRecordingPath;
	}
	
	JnaConnection openByXmlConfig() {
		this.jnaLib = new PnJNALibraryWrapper(this, this);
		this.jnaLib.startByXmlConfig(this.configOrRecordingPath);
		return this;
	}
	
	JnaConnection openByOniRecording() {
		this.jnaLib = new PnJNALibraryWrapper( (OnUserStateChangedCallback) this, (OnJointPositionChangedCallback) this);
		this.jnaLib.startByOniRecording(this.configOrRecordingPath);
		return this;
	}
	
	public void close() {
		LOG.debug("close()");
		
		if(this.jnaLib == null) {
			throw new IllegalStateException("this.gate == null");
		}
		this.jnaLib.shutdown();
	}

	public void onUserStateChanged(int userId, int userState) {
		for(ConnectionListener listener : this.getListeners()) {
			listener.onUserMessage(userId, userState);
		}
	}

	public void onJointPositionChanged(int userId, int jointId, float x, float y, float z) {
		JointMessage message = new JointMessage(userId, new JointData(jointId, new Array3f(x, y, z)));
		for(ConnectionListener listener : this.getListeners()) {
			listener.onJointMessage(message);
		}
	}
}
