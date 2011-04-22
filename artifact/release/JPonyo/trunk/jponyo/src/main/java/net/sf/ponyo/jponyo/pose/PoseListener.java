package net.sf.ponyo.jponyo.pose;

import net.sf.ponyo.jponyo.common.async.Listener;

public interface PoseListener extends Listener {
	
	void onPoseStart();
	
	void onPoseStop();
	
}
