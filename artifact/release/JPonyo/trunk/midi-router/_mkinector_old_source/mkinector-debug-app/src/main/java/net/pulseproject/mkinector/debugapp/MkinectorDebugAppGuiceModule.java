package net.pulseproject.mkinector.debugapp;

import net.pulseproject.mkinector.debugapp.view.UserPanelFactory;
import net.pulseproject.mkinector.debugapp.view.UserPanelImpl;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryProvider;

public class MkinectorDebugAppGuiceModule extends AbstractModule {

	@Override final protected void configure() {
		
		bind(UserPanelFactory.class).toProvider(
				FactoryProvider.newFactory(UserPanelFactory.class, UserPanelImpl.class));
		
	}

}
