package net.sf.ponyo.jponyo.common.async;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Can either be used as an super class (and extending from it) or by delegating to a private member instance.
 * 
 * @since 0.1
 */
public class DefaultAsyncFor<K, L extends Listener> implements AsyncFor<K, L> {
	
	private static final Log LOG = LogFactory.getLog(DefaultAsync.class);
	
	
	private final Map<K, Collection<L>> listenersByKey = new HashMap<K, Collection<L>>();
	

	/** {@inheritDoc} from {@link AsyncFor} */
	public final void addListenerFor(final K key, final L listener) {
		this.beforeAddListener(key, listener);
		this.initListenersFor(key);
		
		final Collection<L> registeredListeners = this.listenersByKey.get(key);
		final boolean wasChanged = registeredListeners.add(listener);
		if(wasChanged == false) {
			LOG.warn("Not added listener [" + listener + "] as it was yet added for key [" + key + "]!");
		}
	}

	/** {@inheritDoc} from {@link AsyncFor} */
	public final void removeListenerFor(final K key, final L listener) {
		this.beforeRemoveListener(key, listener);
		
		final Collection<L> registeredListeners = this.listenersByKey.get(key);
		if(registeredListeners == null) {
			LOG.warn("Not removed listener [" + listener + "] as _nothing_ was yet added for key [" + key + "]!");
			return;
		}
		
		final boolean wasRemoved = registeredListeners.remove(listener);
		if(wasRemoved == false) {
			LOG.warn("Not removed listener [" + listener + "] as it was not yet added for key [" + key + "]!");
		}
	}
	/**
	 * @since 0.4
	 */
	protected final Collection<L> getListenersFor(final K key) {
		this.initListenersFor(key);
		return this.listenersByKey.get(key);
	}
	
	private void initListenersFor(final K key) {
		if(this.listenersByKey.containsKey(key) == false) {
			this.listenersByKey.put(key, new HashSet<L>());
		}
	}
	
	
	/**
	 * Will be used to check if <code>Closeable</code> was not yet closed.
	 * 
	 * @since 0.4
	 */
	@SuppressWarnings("unused")
	protected void beforeAddListener(final K key, final L listener) {
		// can be overridden
	}

	/**
	 * Will be used to check if <code>Closeable</code> was not yet closed.
	 * 
	 * @since 0.4
	 */
	@SuppressWarnings("unused")
	protected void beforeRemoveListener(final K key, final L listener) {
		// can be overridden
	}

}
