package net.sf.ponyo.jponyo.samples;

import net.sf.ponyo.jponyo.connection.jna.JnaModule;
import net.sf.ponyo.jponyo.connection.osc.OscModule;
import net.sf.ponyo.jponyo.core.CoreModule;
import net.sf.ponyo.jponyo.user.UserModule;

import com.google.inject.AbstractModule;

public class SampleJPonyoModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new UserModule());
		install(new OscModule());
		install(new JnaModule());
		install(new CoreModule());
	}

}
