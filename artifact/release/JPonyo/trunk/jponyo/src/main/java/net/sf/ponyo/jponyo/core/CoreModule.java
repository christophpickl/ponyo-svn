package net.sf.ponyo.jponyo.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class CoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ContextStarter.class).to(ContextStarterImpl.class).in(Scopes.SINGLETON);
	}

}
