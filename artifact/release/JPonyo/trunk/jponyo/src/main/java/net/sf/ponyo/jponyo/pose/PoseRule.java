package net.sf.ponyo.jponyo.pose;

import net.sf.ponyo.jponyo.entity.Skeleton;

public abstract class PoseRule {
	
	private final String label;
	
	private boolean active = false;

	public PoseRule(String label) {
		this.label = label;
	}
	
	abstract boolean validate(Skeleton skeleton);

	public final String getLabel() {
		return this.label;
	}
	
	public final boolean isActive() {
		return this.active;
	}
	
}
