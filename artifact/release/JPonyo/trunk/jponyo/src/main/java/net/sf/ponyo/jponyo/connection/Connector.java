package net.sf.ponyo.jponyo.connection;

/**
 * @since 0.1
 */
public interface Connector<T extends Connection> {
	
	T openConnection();
	
}
