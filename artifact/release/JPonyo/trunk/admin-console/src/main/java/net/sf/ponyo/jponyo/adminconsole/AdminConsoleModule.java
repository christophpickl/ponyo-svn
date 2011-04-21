package net.sf.ponyo.jponyo.adminconsole;

import net.sf.ponyo.jponyo.JPonyoModule;

import com.google.inject.AbstractModule;

public class AdminConsoleModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JPonyoModule());
	}

}
