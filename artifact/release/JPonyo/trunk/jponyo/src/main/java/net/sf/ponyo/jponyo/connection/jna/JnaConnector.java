package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;

/**
 * @since 0.1
 */
public class JnaConnector /*extends DefaultAsync*/ implements Connector {
	
	private final JnaGate gate = new JnaGate();
	
	private static final JnaConnector INSTANCE = new JnaConnector();
	
	private JnaConnector() { /*singleton*/ }
	
	public static final JnaConnector getInstance() {
		return INSTANCE;
	}
	
	public Connection openConnection() {
		return null;
	}

}
