package net.sf.ponyo.jponyo.global;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Context implements ConnectionListener {
	
	private static final Log LOG = LogFactory.getLog(Context.class);
	
	private final GlobalSpace space = new GlobalSpace();
	
	private Connection connection;
	
	public void startOniRecording(String oniRecordingPath) {
//		Connector connector = new OscConnector();
		Connector connector = new JnaByRecordingConnector(oniRecordingPath);
//		Connector connector = new JnaByConfigConnector(CONFIG_PATH);
		
		this.connection = connector.openConnection();
		this.connection.addListener(this);
	}
	
	public void shutdown() {
		if(this.connection == null) {
			LOG.warn("Tried to shutdown but there is no connection to close!");
			return;
		}
		this.connection.close();
		this.connection = null;
	}
	
	private int i = 0;
	public void onJointMessage(int userId, int jointId, float x, float y, float z) {
		if(this.i++ == 100) {
			LOG.trace("joint");
		}		
	}

	public void onUserMessage(int userId, int userState) {
		LOG.debug("onUserMessage(userId=" + userId + ", userState=" + userState + ")");
	}
	
}
