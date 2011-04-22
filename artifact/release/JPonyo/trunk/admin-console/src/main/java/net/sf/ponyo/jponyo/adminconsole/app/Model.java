package net.sf.ponyo.jponyo.adminconsole.app;

import net.sf.ponyo.jponyo.common.aop.AopToString;
import net.sf.ponyo.jponyo.common.aop.AopToStringField;
import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.jponyo.common.binding.BindingProvider;

public class Model implements BindingProvider {
	
	@AopToStringField
	private String foobar = "jaaaaaaa";
	
	public Object get(String propertyName) {
		return null;
	}

	public Iterable<BindingListener> getBindingListenersFor(String attributeKey) {
		return null;
	}

	public void set(String propertyName, Object value) {

	}

	public void addListenerFor(String key, BindingListener listener) {

	}

	public void removeListenerFor(String key, BindingListener listener) {

	}

	@AopToString @Override public String toString() { return null; }
}
