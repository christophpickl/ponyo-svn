package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;

/**
 * @since 0.1
 */
public class JnaConnector /*extends DefaultAsync*/ implements Connector {
	
	private final String configPath;
	
	public JnaConnector(String configPath) {
		this.configPath = configPath;
	}
	
	public Connection openConnection() {
		return new JnaConnection(this.configPath).open();
	}
}
