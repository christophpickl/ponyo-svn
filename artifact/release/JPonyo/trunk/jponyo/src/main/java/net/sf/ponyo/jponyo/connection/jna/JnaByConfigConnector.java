package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;

/**
 * @since 0.1
 */
public class JnaByConfigConnector implements Connector {
	
	private final String configXmlPath;

	/**
	 * @since 0.1
	 */
	public JnaByConfigConnector(String configPath) {
		this.configXmlPath = configPath;
	}

	/**
	 * @since 0.1
	 */
	public Connection openConnection() {
		return new JnaConnection(this.configXmlPath).openByXmlConfig();
	}
}
