package net.sf.ponyo.jponyo.common.async;

/**
 * @since 0.4
 */
public interface AsyncFor<K, L extends Listener> {

	/**
	 * @since 0.4
	 */
	void addListenerFor(K key, L listener);

	/**
	 * @since 0.4
	 */
	void removeListenerFor(K key, L listener);
	
}
