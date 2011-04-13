package net.sf.ponyo.jponyo;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class App {
	
	private static final Log LOG = LogFactory.getLog(App.class);
	
	public static final String CONFIG_PATH = "/ponyo/niconfig.xml";
	public static final String ONI_PATH = "/ponyo/oni.oni";
	
	
	public static void main(String[] args) throws Exception {
		LOG.debug("main() START");

		Connector connector = new JnaConnector(CONFIG_PATH);
		Connection connection = connector.openConnection();
		connection.addListener(new ConnectionListener() {
			public void onUserMessage(int userId, int userState) {
				System.out.println("onUserMessage(userId=" + userId + ", userState=" + userState + ")");
			}
			public void onJointMessage(int userId, int jointId, float x, float y, float z) {
				System.out.println("joint");
			}
		});
		System.out.println("Sleeping ...");
		Thread.sleep(3000);
		connection.close();
		
		LOG.debug("main() END");
	}
}
