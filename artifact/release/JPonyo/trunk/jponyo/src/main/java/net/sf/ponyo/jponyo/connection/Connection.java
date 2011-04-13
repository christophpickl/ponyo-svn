package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.Async;
import net.sf.ponyo.jponyo.Closeable;

/**
 * @since 0.1
 */
public interface Connection extends Async<ConnectionListener>, Closeable {
	
}
