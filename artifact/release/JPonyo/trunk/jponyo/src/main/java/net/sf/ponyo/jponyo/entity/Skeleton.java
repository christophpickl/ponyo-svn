package net.sf.ponyo.jponyo.entity;

import net.sf.ponyo.jponyo.common.math.Array3f;
import net.sf.ponyo.jponyo.connection.JointData;

/**
 * @since 0.1
 */
public class Skeleton {
	
	private final Array3f[] coordinatesByJoint = new Array3f[24];
	{
		// initialize data
		Array3f emptyCoordinates = new Array3f(0.0f, 0.0f, 0.0f);
		for(Joint joint : Joint.values()) {
			this.coordinatesByJoint[joint.getId()] = emptyCoordinates;
		}
	}
	
	public Array3f getCoordinates(Joint joint) {
		return this.coordinatesByJoint[joint.getId()];
	}

	public void update(JointData data) {
		final int jointId = data.getJointId();
		this.coordinatesByJoint[jointId] = data.getJointPosition();
	}
	
}
