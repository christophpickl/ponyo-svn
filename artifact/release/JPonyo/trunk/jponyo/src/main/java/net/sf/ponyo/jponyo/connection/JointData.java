package net.sf.ponyo.jponyo.connection;

import net.sf.ponyo.jponyo.common.math.Array3f;

public class JointData {

	private final int/*TODO change to byte?!*/ jointId;
	private final Array3f jointPosition;
	
	public JointData(int jointId, Array3f jointPosition) {
		this.jointId = jointId;
		this.jointPosition = jointPosition;
	}
	public int getJointId() {
		return this.jointId;
	}
	public Array3f getJointPosition() {
		return this.jointPosition;
	}

	@Override
	public String toString() {
		return "JointData[jointId=" + this.jointId + ", jointPosition=" + this.jointPosition + "]";
	}
	
}
