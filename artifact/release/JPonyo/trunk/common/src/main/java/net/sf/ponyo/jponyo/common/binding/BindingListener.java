package net.sf.ponyo.jponyo.common.binding;

import net.sf.ponyo.jponyo.common.async.Listener;

public interface BindingListener extends Listener {
	
	void onValueChanged(Object newValue);
	
}
