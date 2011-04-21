package net.sf.ponyo.jponyo.connection.jna;


public interface JnaByConfigConnectorFactory {
	
	JnaByConfigConnectorImpl create(String configXmlPath);
	
}
