package net.sf.ponyo.jponyo.entity;

/**
 * @since 0.1
 */
public enum Joint {

	HEAD(0, "Head"),
	NECK(1, "Neck"),
	TORSO(2, "Torso"),
	LEFT_SHOULDER(5, "Left Shoulder"),
	LEFT_ELBOW(6, "Left Elbow"),
	LEFT_HAND(8, "Left Hand"),
	RIGHT_SHOULDER(11, "Right Shoulder"),
	RIGHT_ELBOW(12, "Right Elbow"),
	RIGHT_HAND(14, "Right Hand"),
	LEFT_HIP(16, "Left Hip"),
	LEFT_KNEE(17, "Left Knee"),
	LEFT_FOOT(19, "Left Foot"),
	RIGHT_HIP(20, "Right Hip"),
	RIGHT_KNEE(21, "Right Knee"),
	RIGHT_FOOT(23, "Right Foot"),
	;
	
	public static final int MAX_JOINT_ID = 23;
	private static final Joint[] JOINT_ENUM_BY_ID = new Joint[MAX_JOINT_ID + 1];
	static {
		for(Joint joint : Joint.values()) {
			JOINT_ENUM_BY_ID[joint.getId()] = joint;
		}
	}
	
	private final int id;
	private final String label;
	
	private Joint(int id, String label) {
		this.id = id;
		this.label = label;
	}
	
	public int getId() {
		return this.id;
	}
	public String getLabel() {
		return this.label;
	}
	
	public static Joint byId(int id) {
		return JOINT_ENUM_BY_ID[id];
	}
}
