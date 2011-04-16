package net.sf.ponyo.jponyo.connection.jna;



/**
 * @since 0.1
 */
public class JnaByConfigConnector extends AbstractJnaConnector {
	
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
	public JnaConnection openConnection() {
		return new JnaConnection(this.configXmlPath).openByXmlConfig();
	}

}
