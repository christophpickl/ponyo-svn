package net.sf.ponyo.jponyo.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserState;

/**
 * @since 0.1
 */
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
	
	public Collection<User> getFilteredUsers(UserState state) {
		final Set<User> filteredUsers = new HashSet<User>();
		for (User user : this.users) {
			if(user.getState() == state) {
				filteredUsers.add(user);
			}
		}
//		System.out.println("getFilteredUsers(state="+state+") ... this.users: " + this.users + "; returning: " + filteredUsers);
		return filteredUsers;
	}
	
}
