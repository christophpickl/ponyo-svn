package net.pulseproject.mkinector.debugapp.misc;

import net.pulseproject.mkinector.debugapp.view.UserPanel;

public interface OscConnectionWindowGlueListener {
	
	void onAddUserPanel(final UserPanel userPanel);
	
	void onRemoveUserPanel(UserPanel userPanel);
	
	void onUserCountChanged(final int userReadyCount, final int userWaitingCount);
	
}
