package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.user.UserManager;

/**
 * @since 0.1
 */
public interface Connector<T extends Connection> {
	
	T openConnection();

	UserManager createUserManager();
	
}
