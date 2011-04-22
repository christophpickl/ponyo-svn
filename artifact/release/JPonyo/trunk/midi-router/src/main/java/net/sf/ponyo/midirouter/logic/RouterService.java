package net.sf.ponyo.midirouter.logic;

import net.sf.ponyo.console.view.ConsoleDialog;

interface RouterService {
	
	void start();

	void updateMappings();

	void stop();

	void manage(ConsoleDialog adminDialog);
	
}