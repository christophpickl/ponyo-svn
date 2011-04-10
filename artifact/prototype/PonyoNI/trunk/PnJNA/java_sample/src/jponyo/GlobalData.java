package jponyo;

import jponyo.jna.Skel;

public class GlobalData {
	public final float[] xByJoint = new float[Skel.COUNT];
	public final float[] yByJoint = new float[Skel.COUNT];
	public final float[] zByJoint = new float[Skel.COUNT];
}
