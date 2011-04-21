package net.sf.ponyo.jponyo;

import net.sf.ponyo.jponyo.connection.ConnectionModule;
import net.sf.ponyo.jponyo.core.CoreModule;
import net.sf.ponyo.jponyo.user.UserModule;

import com.google.inject.AbstractModule;

public class JPonyoModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConnectionModule());
		install(new CoreModule());
		install(new UserModule());
	}

}
