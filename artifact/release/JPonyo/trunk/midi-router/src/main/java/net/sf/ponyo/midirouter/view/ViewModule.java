package net.sf.ponyo.midirouter.view;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ViewModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(MidiPortsDialog.class).toProvider(MidiPortsDialogProvider.class);

		bind(MainView.class).to(MainViewImpl.class).in(Scopes.SINGLETON);
		
	}

}
