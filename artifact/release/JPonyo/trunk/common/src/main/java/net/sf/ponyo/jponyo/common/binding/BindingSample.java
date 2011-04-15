package net.sf.ponyo.jponyo.common.binding;

import net.sf.ponyo.jponyo.common.async.DefaultAsyncFor;


public class BindingSample {
	public static void main(String[] args) {
		Model model = new Model();
		model.addListenerFor(Model.FOO, new BindingListener() {
			public void onValueChanged(Object newValue) {
				System.out.println("onValueChanged(newValue=" + newValue + ")");
				// onValueChanged(newValue=init)
				// onValueChanged(newValue=bar)
			}
		});
		
		model.setFoo("bar");
	}
	
	public static class Model extends DefaultAsyncFor<String, BindingListener> implements BindingProvider {
		
		public static final String FOO = "FOO";
		private String foo = "init";
		public String getFoo() { return this.foo; }
		@BindingSetter(Key = FOO) public void setFoo(String foo) { this.foo = foo; }
		
		public Iterable<BindingListener> getBindingListenersFor(String attributeKey) {
			// TODO das bekommt man auch noch raus => DefaultAsyncFor muss teilweise dafuer ein interface hergeben, wo dann aspekt getListenersFor direkt aufrufen kann!
			return this.getListenersFor(attributeKey);
		}
		
	}
}
