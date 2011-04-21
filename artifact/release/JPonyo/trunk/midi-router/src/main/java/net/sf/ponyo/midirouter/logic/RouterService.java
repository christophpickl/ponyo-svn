package net.sf.ponyo.midirouter.logic;

import net.sf.ponyo.jponyo.adminconsole.view.AdminDialog;

interface RouterService {
	
	void start();

	void updateMappings();

	void stop();

	void manage(AdminDialog adminDialog);
	
}