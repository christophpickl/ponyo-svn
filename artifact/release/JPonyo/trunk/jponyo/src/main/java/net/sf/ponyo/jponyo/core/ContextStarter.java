package net.sf.ponyo.jponyo.core;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnector;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;
import net.sf.ponyo.jponyo.user.UserManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 0.1
 */
public class ContextStarter {
	
	private static final Log LOG = LogFactory.getLog(ContextStarter.class);
	
	public Context startXmlConfig(String xmlConfigPath) {
		return this.internalStart(new JnaByConfigConnector(xmlConfigPath));
	}

	public Context startOniRecording(String oniRecordingPath) {
		return this.internalStart(new JnaByRecordingConnector(oniRecordingPath));
	}

	public Context startOscReceiver() {
		return this.internalStart(new OscConnector());
	}

	private Context internalStart(Connector<? extends Connection> connector) {
		LOG.debug("internalStart(..) START");
		
		Connection connection = connector.openConnection();
		
		Context context = new Context(connection);
		UserManager userManager = connector.createUserManager(context);
		context.setUserManager(userManager); // TODO yes, we do have a cyclic dependency here ;)
		
		connection.addListener(context);
		context.start();
		
		LOG.debug("internalStart(..) END");
		return context;
		
	}
}
