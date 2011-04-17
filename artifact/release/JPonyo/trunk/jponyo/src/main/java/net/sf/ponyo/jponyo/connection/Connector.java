package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.user.UserManager;
import net.sf.ponyo.jponyo.user.UserManagerCallback;

/**
 * @since 0.1
 */
public interface Connector<T extends Connection> {
	
	T openConnection();

	UserManager createUserManager(UserManagerCallback callback);
	
}
