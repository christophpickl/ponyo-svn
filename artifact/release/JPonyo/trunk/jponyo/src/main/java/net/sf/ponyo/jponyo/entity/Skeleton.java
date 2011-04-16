package net.sf.ponyo.jponyo.entity;

/**
 * @since 0.1
 */
public class Skeleton {
	
	private final float[][] coordinatesByJoint = new float[24][3];
	{
		this.coordinatesByJoint[Joint.HEAD.getId()] = new float[] { 0.0f, 0.0f, 0.0f };
		this.coordinatesByJoint[Joint.NECK.getId()] = new float[] { 0.0f, 0.0f, 0.0f };
	}
	
	public void updateJoint(int jointId, float x, float y, float z) {
		this.coordinatesByJoint[jointId][0] = x;
		this.coordinatesByJoint[jointId][1] = y;
		this.coordinatesByJoint[jointId][2] = z;
	}
	
	public float[] getCoordinates(Joint joint) {
		return this.coordinatesByJoint[joint.getId()];
	}

}
