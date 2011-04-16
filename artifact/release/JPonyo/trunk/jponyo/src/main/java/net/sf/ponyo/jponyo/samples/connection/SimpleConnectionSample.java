package net.sf.ponyo.jponyo.samples.connection;

import net.sf.ponyo.jponyo.DevelopmentConstants;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnector;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleConnectionSample {
	
	private static final Log LOG = LogFactory.getLog(SimpleConnectionSample.class);
	
	private static int i;
	
	// force imports
	@SuppressWarnings("unused") private OscConnector c1;
	@SuppressWarnings("unused") private JnaByRecordingConnector c2;
	@SuppressWarnings("unused") private JnaByConfigConnector c3;
	
	public static void main(String[] args) throws Exception {
		LOG.debug("main() START");

//		Connector<? extends Connection> connector = new OscConnector();
		Connector<? extends Connection> connector = new JnaByRecordingConnector(DevelopmentConstants.ONI_PATH);
//		Connector<? extends Connection> connector = new JnaByConfigConnector(Constants.XML_PATH);
		
		Connection connection = connector.openConnection();
		connection.addListener(new ConnectionListener() {
			public void onUserMessage(int userId, int userState) {
				LOG.debug("onUserMessage(userId=" + userId + ", userState=" + userState + ")");
			}
			public void onJointMessage(int userId, int jointId, float x, float y, float z) {
				if(i++ == 100) {
					LOG.trace("joint");
				}
			}
		});
		System.out.println("Sleeping ...");
		Thread.sleep(5000);
		connection.close();
		
		LOG.debug("main() END");
	}
}
