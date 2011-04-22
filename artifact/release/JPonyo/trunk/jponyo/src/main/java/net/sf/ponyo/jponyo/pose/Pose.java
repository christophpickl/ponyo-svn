package net.sf.ponyo.jponyo.pose;

import net.sf.ponyo.jponyo.common.async.Async;
import net.sf.ponyo.jponyo.stream.MotionStream;
import net.sf.ponyo.jponyo.user.User;

public interface Pose extends Async<PoseListener> {
	
	String getLabel();
	
	void startDetecting(User user, MotionStream stream);
	void stopDetecting();
	boolean isDetecting();
	
}
