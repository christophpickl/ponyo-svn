package net.sf.ponyo.midirouter.view;

import net.sf.ponyo.midirouter.refactor.ButtonBarListener;
import net.sf.ponyo.midirouter.view.framework.AbstractMainViewListener;

public interface MainViewListener
	extends AbstractMainViewListener, ButtonBarListener {

	void onToggleMidiPortsWindow();

	void onToggleHelpWindow();

	void onToggleAdminConsole();

}
