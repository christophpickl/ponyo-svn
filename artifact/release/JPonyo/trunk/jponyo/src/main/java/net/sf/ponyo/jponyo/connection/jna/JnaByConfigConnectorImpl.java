package net.sf.ponyo.jponyo.connection.jna;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * @since 0.1
 */
public class JnaByConfigConnectorImpl extends AbstractJnaConnector {
	
	private static final Log LOG = LogFactory.getLog(JnaByConfigConnectorImpl.class);
	
	private final String configXmlPath;

	/**
	 * @since 0.1
	 */
	@Inject
	public JnaByConfigConnectorImpl(@Assisted String configPath) {
		LOG.debug("new JnaByConfigConnector(..)");
		this.configXmlPath = configPath;
	}

	/**
	 * @since 0.1
	 */
	public JnaConnection openConnection() {
		return new JnaConnection(this.configXmlPath).openByXmlConfig();
	}

}
