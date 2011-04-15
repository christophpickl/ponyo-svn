package net.sf.ponyo.jponyo.samples.connection;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleConnectionSample {
	
	private static final Log LOG = LogFactory.getLog(SimpleConnectionSample.class);
	
	public static final String CONFIG_PATH = "/ponyo/niconfig.xml";
	public static final String ONI_PATH = "/ponyo/oni.oni";
	
	private static int i;
	
	public static void main(String[] args) throws Exception {
		LOG.debug("main() START");

		Connector connector = new OscConnector();
//		Connector connector = new JnaByRecordingConnector(ONI_PATH);
//		Connector connector = new JnaByConfigConnector(CONFIG_PATH);
		
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
		Thread.sleep(3000);
		connection.close();
		
		LOG.debug("main() END");
	}
}
