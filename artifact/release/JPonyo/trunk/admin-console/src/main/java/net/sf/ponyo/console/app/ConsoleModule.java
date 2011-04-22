package net.sf.ponyo.console.app;

import net.sf.ponyo.jponyo.JPonyoModule;

import com.google.inject.AbstractModule;

public class ConsoleModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JPonyoModule());
		
		bind(Model.class).toInstance(new Model());
	}

}
