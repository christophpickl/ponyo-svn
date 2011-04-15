package net.sf.ponyo.jponyo.common.async;

/**
 * Compared to <code>java.io.Closeable</code> this interface does not declare a checked exception to be thrown.
 * 
 * Onca a <code>Closeable</code> has been closed, any further method calls will respond with an 
 * <code>IllegalStateException</code>.
 * 
 * @since 0.1
 */
public interface Closeable {

	/**
	 * It is most likely you will get an <code>IllegalStateException</code> if called more than once.
	 * 
	 * @since 0.1
	 */
	void close();
	
}
