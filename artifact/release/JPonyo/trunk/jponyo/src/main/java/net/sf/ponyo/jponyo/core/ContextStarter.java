package net.sf.ponyo.jponyo.core;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnector;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 0.1
 */
public class ContextStarter {
	
	private static final Log LOG = LogFactory.getLog(ContextStarter.class);
	
	public Context startXmlConfig(String xmlConfigPath) {
		InternalContext iContext = new InternalContext();
		return this.internalStart(new JnaByConfigConnector(xmlConfigPath), iContext);
	}

	public Context startOniRecording(String oniRecordingPath) {
		InternalContext iContext = new InternalContext();
		return this.internalStart(new JnaByRecordingConnector(oniRecordingPath), iContext);
	}

	public Context startOscReceiver() {
		// pass this == UserManagerCallback to artificially create user events if we are using OSC
		InternalContext iContext = new InternalContext();
		return this.internalStart(new OscConnector(iContext), iContext);
	}

	private Context internalStart(Connector<? extends Connection> connector, InternalContext iContext) {
		LOG.debug("internalStart(..) START");
		
		Connection connection = connector.openConnection();
		Context context = new Context(connection, iContext, connector.createUserManager());
		connection.addListener(context);
		
		LOG.debug("internalStart(..) END");
		return context;
		
	}
}
