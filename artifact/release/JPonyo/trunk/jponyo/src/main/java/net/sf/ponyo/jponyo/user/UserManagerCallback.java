package net.sf.ponyo.jponyo.user;

public interface UserManagerCallback {

	void processUserStateChange(User user, UserState state);
	
}
