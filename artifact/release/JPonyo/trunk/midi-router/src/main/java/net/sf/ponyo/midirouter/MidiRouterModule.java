package net.sf.ponyo.midirouter;

import net.sf.ponyo.jponyo.JPonyoModule;
import net.sf.ponyo.jponyo.connection.jna.JnaModule;
import net.sf.ponyo.jponyo.connection.osc.OscModule;
import net.sf.ponyo.jponyo.core.CoreModule;
import net.sf.ponyo.jponyo.user.UserModule;
import net.sf.ponyo.midirouter.logic.LogicModule;
import net.sf.ponyo.midirouter.logic.midi.MidiModule;
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
