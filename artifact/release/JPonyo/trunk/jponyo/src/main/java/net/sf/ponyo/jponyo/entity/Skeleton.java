package net.sf.ponyo.jponyo.entity;

import net.sf.ponyo.jponyo.connection.JointData;

/**
 * @since 0.1
 */
public class Skeleton {
	
	private final float[][] coordinatesByJoint = new float[24][3];
	{
		this.coordinatesByJoint[Joint.HEAD.getId()] = new float[] { 0.0f, 0.0f, 0.0f };
		this.coordinatesByJoint[Joint.NECK.getId()] = new float[] { 0.0f, 0.0f, 0.0f };
	}
	
	public float[] getCoordinates(Joint joint) {
		return this.coordinatesByJoint[joint.getId()];
	}

	public void update(JointData data) {
		final int jointId = data.getJointId();
		this.coordinatesByJoint[jointId] = data.getJointPosition().data;
	}
	
}
