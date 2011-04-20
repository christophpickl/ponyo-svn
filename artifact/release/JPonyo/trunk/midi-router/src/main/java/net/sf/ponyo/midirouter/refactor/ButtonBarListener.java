package net.sf.ponyo.midirouter.refactor;

import net.sf.ponyo.jponyo.common.async.Listener;

public interface ButtonBarListener extends Listener {
	
	void onStartStopClicked();

	void onReloadClicked();
	
}
