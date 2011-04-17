package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.user.FastUserManager;
import net.sf.ponyo.jponyo.user.UserManager;
import net.sf.ponyo.jponyo.user.UserManagerCallback;

/**
 * @since 0.1
 */
abstract class AbstractJnaConnector implements JnaConnector {

	public final UserManager createUserManager(UserManagerCallback callback) {
		return new FastUserManager();
	}
}
