package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.common.async.Async;
import net.sf.ponyo.jponyo.common.async.Closeable;

/**
 * @since 0.1
 */
public interface Connection extends Async<ConnectionListener>, Closeable {
	
}
