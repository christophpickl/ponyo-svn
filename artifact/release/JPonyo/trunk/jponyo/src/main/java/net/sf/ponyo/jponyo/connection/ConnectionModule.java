package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.connection.jna.JnaModule;
import net.sf.ponyo.jponyo.connection.osc.OscModule;

import com.google.inject.AbstractModule;

public class ConnectionModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new OscModule());
		install(new JnaModule());
	}
	
}
