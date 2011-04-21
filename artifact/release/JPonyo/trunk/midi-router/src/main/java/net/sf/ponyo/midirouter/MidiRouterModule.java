package net.sf.ponyo.midirouter;

import net.sf.ponyo.jponyo.JPonyoModule;
import net.sf.ponyo.midirouter.logic.LogicModule;
import net.sf.ponyo.midirouter.view.ViewModule;

import com.google.inject.AbstractModule;

public class MidiRouterModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JPonyoModule());
		
		install(new LogicModule()); // contains: MidiModule
		install(new ViewModule());
	}

}
