package net.sf.ponyo.jponyo.entity;

public class Skeleton {
	
	private final float[][] coordinatesByJoint = new float[2][3];
	{
		this.coordinatesByJoint[Joint.HEAD.getId()] = new float[] { 0.0f, 0.0f, 0.0f };
		this.coordinatesByJoint[Joint.TORSO.getId()] = new float[] { 0.0f, 0.0f, 0.0f };
	}
	
	public void updateJoint(int jointId, float x, float y, float z) {
		this.coordinatesByJoint[jointId][0] = x;
		this.coordinatesByJoint[jointId][1] = y;
		this.coordinatesByJoint[jointId][2] = z;
	}
	
	public float[] getCoordinates(int jointId) { // TODO by jointId!?!
		assert(jointId > 0 && jointId < Joint.MAX_JOINT_ID);
		return this.coordinatesByJoint[jointId];
	}

}
