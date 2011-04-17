package net.sf.ponyo.jponyo.stream;

import net.sf.ponyo.jponyo.common.math.Array3f;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.user.User;

/**
 * @since 0.1
 */
public class MotionData {

//	* Most likely only the <code>skeleton</code> parameter will be used to do a full re-check of joint coordinates.
////	 * @param skeleton as it is most likely one wants to recheck all conditions when single joint moved
//	Joint joint, float x, float y, float z /*, Skeleton skeleton*/
	
	private final User user;
	private final Joint joint;
	private final Array3f jointPosition;
	
	public MotionData(User user, Joint joint, Array3f jointPosition) {
		this.user = user;
		this.joint = joint;
		this.jointPosition = jointPosition;
	}
	public User getUser() {
		return this.user;
	}
	public Joint getJoint() {
		return this.joint;
	}
	public Array3f getJointPosition() {
		return this.jointPosition;
	}

	@Override
	public String toString() {
		return "JointData[user=" + this.user + ", jointPosition=" + this.jointPosition + "]";
	}
}
