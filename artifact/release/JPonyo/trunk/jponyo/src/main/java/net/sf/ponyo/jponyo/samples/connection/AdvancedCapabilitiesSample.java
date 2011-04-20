package net.sf.ponyo.jponyo.samples.connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.connection.AdvancedCapabilities;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;
import net.sf.ponyo.jponyo.core.DevelopmentConstants;

public class AdvancedCapabilitiesSample {
	
	private static final Log LOG = LogFactory.getLog(AdvancedCapabilitiesSample.class);
	
	public static void main(String[] args) {
		LOG.debug("main() START");
		Connection connection = new JnaByRecordingConnector(DevelopmentConstants.ONI_PATH).openConnection();
		
		if(connection.hasAdvancedCapabilities() == false) {
			System.err.println("Has no advanced capabilities!");
		} else {
			AdvancedCapabilities advanced = connection.getAdvancedCapabilities();
			System.out.println("OpenNI Version: " + advanced.getOpenNIVersion());
			System.out.println("Ponyo Version: " + advanced.getPonyoVersion());
		}
		
		connection.close();
		LOG.debug("main() END");
	}
	
}
