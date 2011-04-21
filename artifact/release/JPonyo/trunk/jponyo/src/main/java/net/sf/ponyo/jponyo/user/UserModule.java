package net.sf.ponyo.jponyo.user;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class UserModule extends AbstractModule {
	
	@Override
	protected void configure() {
		
		install(new FactoryModuleBuilder()
			.implement(UserManager.class, RunningSessionUserManager.class)
			.build(RunningSessionUserManagerFactory.class));
		
		
	}

}
