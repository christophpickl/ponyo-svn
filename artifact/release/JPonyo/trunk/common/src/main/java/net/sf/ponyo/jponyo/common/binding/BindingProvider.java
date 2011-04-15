package net.sf.ponyo.jponyo.common.binding;

import net.sf.ponyo.jponyo.common.async.AsyncFor;

public interface BindingProvider extends AsyncFor<String, BindingListener> {
	
	Iterable<BindingListener> getBindingListenersFor(String attributeKey);
	
}
