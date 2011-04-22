package net.sf.ponyo.midirouter.view;

import net.sf.ponyo.jponyo.common.gui.AbstractMainViewListener;
import net.sf.ponyo.midirouter.refactor.ButtonBarListener;

public interface MainViewListener
	extends AbstractMainViewListener, ButtonBarListener {

	void onToggleMidiPortsWindow();

	void onToggleHelpWindow();

	void onToggleAdminConsole();

}
