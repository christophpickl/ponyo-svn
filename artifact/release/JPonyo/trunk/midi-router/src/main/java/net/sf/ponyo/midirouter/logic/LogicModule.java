package net.sf.ponyo.midirouter.logic;

import net.sf.ponyo.midirouter.logic.midi.MidiModule;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class LogicModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new MidiModule());
		
		bind(Model.class).toInstance(new Model());
		bind(MainPresenter.class).to(MainPresenterImpl.class).in(Scopes.SINGLETON);
		bind(RouterService.class).to(RouterServiceImpl.class).in(Scopes.SINGLETON);
		
	}

}
