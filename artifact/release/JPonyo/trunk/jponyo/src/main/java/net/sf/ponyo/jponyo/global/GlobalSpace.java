package net.sf.ponyo.jponyo.global;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.ponyo.jponyo.entity.User;

public class GlobalSpace {
	
	private final Set<User> users = new HashSet<User>();
	
	public void addUser(User user) {
		boolean changed = this.users.add(user);
		assert(changed == true);
	}

	public void removeUser(User user) {
		boolean changed = this.users.remove(user);
		assert(changed == true);
	}
	
	public Collection<User> getUsers() {
		return this.users;
	}
	
}
