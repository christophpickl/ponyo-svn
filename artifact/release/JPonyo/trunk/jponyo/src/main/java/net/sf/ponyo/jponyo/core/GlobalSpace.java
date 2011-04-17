package net.sf.ponyo.jponyo.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.user.User;

/**
 * @since 0.1
 */
public class GlobalSpace {
	
	private static final Log LOG = LogFactory.getLog(GlobalSpace.class);
	
	private final Set<User> users = new HashSet<User>();
	
	public void addUser(User user) {
		LOG.info("addUser(user="+user+")");
		boolean changed = this.users.add(user);
		assert(changed == true);
	}

	public void removeUser(User user) {
		LOG.info("removeUser(user="+user+")");
		boolean changed = this.users.remove(user);
		assert(changed == true);
	}
	
	public Collection<User> getUsers() {
		return this.users;
	}
	
	public boolean isFooTracking = false;
	
}
