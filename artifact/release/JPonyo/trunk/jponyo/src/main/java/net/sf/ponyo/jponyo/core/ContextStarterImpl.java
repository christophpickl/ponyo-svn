package net.sf.ponyo.jponyo.core;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnectorFactory;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnectorFactory;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnectorFactory;
import net.sf.ponyo.jponyo.user.UserManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;

/**
 * @since 0.1
 */
public class ContextStarterImpl implements ContextStarter {
	
	private static final Log LOG = LogFactory.getLog(ContextStarterImpl.class);
	
	private final JnaByConfigConnectorFactory jnaConfigFactory;
	private final JnaByRecordingConnectorFactory jnaRecordingFactory;
	private final OscConnectorFactory oscFactory;
	
	@Inject
	public ContextStarterImpl(
			JnaByConfigConnectorFactory jnaConfigFactory,
			JnaByRecordingConnectorFactory jnaRecordingFactory,
			OscConnectorFactory oscFactory) {
		LOG.debug("new ContextStarterImpl()");
		this.jnaConfigFactory = jnaConfigFactory;
		this.jnaRecordingFactory = jnaRecordingFactory;
		this.oscFactory = oscFactory;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.ponyo.jponyo.core.ContextStarter#startXmlConfig(java.lang.String)
	 */
	public Context startXmlConfig(String configXmlPath) {
		return this.internalStart(this.jnaConfigFactory.create(configXmlPath));
	}

	/* (non-Javadoc)
	 * @see net.sf.ponyo.jponyo.core.ContextStarter#startOniRecording(java.lang.String)
	 */
	public Context startOniRecording(String recordingOniPath) {
		return this.internalStart(this.jnaRecordingFactory.create(recordingOniPath));
	}

	/* (non-Javadoc)
	 * @see net.sf.ponyo.jponyo.core.ContextStarter#startOscReceiverOnPort(int)
	 */
	public Context startOscReceiverOnPort(int port) {
		return this.internalStart(this.oscFactory.create(port));
	}
	
	/* (non-Javadoc)
	 * @see net.sf.ponyo.jponyo.core.ContextStarter#startOscReceiver()
	 */
	public Context startOscReceiver() {
		return this.internalStart(this.oscFactory.create(OscConnector.DEFAULT_PORT));
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
