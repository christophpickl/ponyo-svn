package net.sf.ponyo.jponyo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.connector.jna.JnaGateImpl;

public class App {
	
	private static final Log LOG = LogFactory.getLog(App.class);
	
	public static void main(String[] args) {
		LOG.debug("main() START");
		
		JnaGateImpl gate = new JnaGateImpl();
		try {
			LOG.info("Starting JNA");
//			gate.startWithXml("/ponyo/niconfig.xml");
			gate.startRecording("/ponyo/oni.oni");
		} finally {
			LOG.info("Destroying JNA");
			gate.destroy();
		}
		
		LOG.debug("main() END");
	}
}
