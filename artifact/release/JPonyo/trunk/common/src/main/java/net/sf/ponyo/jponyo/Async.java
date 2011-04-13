package net.sf.ponyo.jponyo;

/**
 * Marks a type to interact asynchroniously via a listener defining some callback methods.
 * 
 * No presumptions about the order of notification can be made, as it will depend on the internally used datastructure.
 * 
 * @since 0.1
 */
public interface Async<L extends Listener> {

	/**
	 * Add a listener to this <code>Async</code> type.
	 * 
	 * If <code>listener</code> was already added, nothing will happen (except of a warning showing up in the logs).
	 * 
	 * @param listener which will be notified via <code>onXyz()</code> callbacks.
	 * @since 0.1
	 */
	void addListener(L listener);

	/**
	 * Add a listener to this <code>Async</code> type.
	 * 
	 * If <code>listener</code> was already added, nothing will happen (except of a warning showing up in the logs).
	 * 
	 * @param listener which will be notified via <code>onXyz()</code> callbacks.
	 * @since 0.1
	 */
	void removeListener(L listener);
	
}
