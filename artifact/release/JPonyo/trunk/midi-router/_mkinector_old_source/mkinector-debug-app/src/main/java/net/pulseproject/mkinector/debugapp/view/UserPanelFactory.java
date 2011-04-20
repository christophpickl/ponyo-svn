package net.pulseproject.mkinector.debugapp.view;

import net.pulseproject.mkinector.josceleton.api.user.User;

public interface UserPanelFactory {
	
	UserPanel create(User user);
	
}
