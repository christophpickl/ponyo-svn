package jponyo;

import jponyo.jna.Skel;

public class GlobalData { // TODO could implement some JointPositionChanged only interface, to update itself
	public boolean isTracking = false;
	public final float[] xByJoint = new float[Skel.COUNT];
	public final float[] yByJoint = new float[Skel.COUNT];
	public final float[] zByJoint = new float[Skel.COUNT];
}
