package net.sf.ponyo.jponyo.user;

public interface RunningSessionUserManagerFactory {
	
	RunningSessionUserManager create(UserManagerCallback callback);
	
}
