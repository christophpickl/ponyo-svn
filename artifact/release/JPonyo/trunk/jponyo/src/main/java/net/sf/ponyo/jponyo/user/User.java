package net.sf.ponyo.jponyo.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.exception.InvalidArgumentException;
import net.sf.ponyo.jponyo.entity.Skeleton;

/**
 * @since 0.1
 */
public class User {
	
	private static final Log LOG = LogFactory.getLog(User.class);
	
	private final int uniqueId;
	private final int openniId;
	private final int color;
	
	private final Skeleton skeleton = new Skeleton();
	private UserState state = UserState.NEW;
	
	public User(int uniqueId, int openniId, int color) {
		if(uniqueId < 0) {
			throw InvalidArgumentException.newInstance("uniqueId", Integer.valueOf(uniqueId), "< 0");
		}
		if(openniId < 1) {
			throw InvalidArgumentException.newInstance("openniId", Integer.valueOf(openniId), "< 1");
		}
		
		// NO!!! if(uniqueId < osceletonId) { // internal (unique) ID must always be >= than (ID-reusing) osceleton ID
		// come on, dont be that restrictive :) ... we could, for example, replay a recorded osceleton session.
		
		this.uniqueId = uniqueId;
		this.openniId = openniId;
		this.color = color;
	}
	
	public final Skeleton getSkeleton() {
		return this.skeleton;
	}
	
	public final UserState getState() {
		return this.state;
	}
	
	public final void setState(UserState state) {
		LOG.debug("setState(state="+state+") for user with uniqueId: " + this.uniqueId);
		this.state = state;
	}

	public final int getUniqueId() {
		return this.uniqueId;
	}

	public final int getOpenniId() {
		return this.openniId;
	}

	public final int getColor() {
		return this.color;
	}

	@Override public boolean equals(final Object other) {
		if(this == other) { return true; }
		if((other instanceof User) == false) { return false; }
		final User that = (User) other;
		return
			this.getUniqueId() == that.getUniqueId() &&
			this.getOpenniId() == that.getOpenniId() &&
			this.getColor() == that.getColor();
	}
	
	@Override public int hashCode() {
		return Integer.valueOf(this.uniqueId).hashCode();
	}
	
	@Override public String toString() {
		return "User[uniqueId=" + this.uniqueId + ", openniId=" + this.openniId + ", state=" + this.state + ", color=" + this.color + "]";
	}
	
}
