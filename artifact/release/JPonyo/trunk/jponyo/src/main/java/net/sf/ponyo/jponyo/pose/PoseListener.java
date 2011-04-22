package net.sf.ponyo.jponyo.pose;

import net.sf.ponyo.jponyo.common.async.Listener;

public interface PoseListener extends Listener {
	
	void onPoseEntered();
	
	void onPoseLeft();
	
	void onPoseRuleChanged(PoseRule rule);
	
}
