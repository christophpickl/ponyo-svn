package net.sf.ponyo.midirouter.logic.midi;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class MidiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MidiConnector.class).to(MidiConnectorImpl.class).in(Scopes.SINGLETON);
	}

}
