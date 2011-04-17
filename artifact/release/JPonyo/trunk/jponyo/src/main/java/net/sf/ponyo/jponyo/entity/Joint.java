package net.sf.ponyo.jponyo.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 0.1
 */
public enum Joint {

	HEAD(0, "Head", "head"),
	NECK(1, "Neck", "neck"),
	TORSO(2, "Torso", "torso"),
	LEFT_SHOULDER(5, "Left Shoulder", "l_shoulder"),
	LEFT_ELBOW(6, "Left Elbow", "l_elbow"),
	LEFT_HAND(8, "Left Hand", "l_hand"),
	RIGHT_SHOULDER(11, "Right Shoulder", "r_shoulder"),
	RIGHT_ELBOW(12, "Right Elbow", "r_elbow"),
	RIGHT_HAND(14, "Right Hand", "r_hand"),
	LEFT_HIP(16, "Left Hip", "l_hip"),
	LEFT_KNEE(17, "Left Knee", "l_knee"),
	LEFT_FOOT(19, "Left Foot", "l_foot"),
	RIGHT_HIP(20, "Right Hip", "r_hip"),
	RIGHT_KNEE(21, "Right Knee", "r_knee"),
	RIGHT_FOOT(23, "Right Foot", "r_foot")
	;
	
	public static final int MAX_JOINT_ID = 23;
	private static final Map<String, Joint> JOINT_ENUM_BY_STRING_ID = new HashMap<String, Joint>();
	private static final Joint[] JOINT_ENUM_BY_ID = new Joint[MAX_JOINT_ID + 1];
	static {
		for(Joint joint : Joint.values()) {
			JOINT_ENUM_BY_ID[joint.getId()] = joint;
			JOINT_ENUM_BY_STRING_ID.put(joint.getStringId(), joint);
		}
	}
	
	private final int id;
	private final String label;
	private final String stringId;
	
	private Joint(int id, String label, String stringId) {
		this.id = id;
		this.label = label;
		this.stringId = stringId;
	}
	
	public int getId() {
		return this.id;
	}
	public String getLabel() {
		return this.label;
	}
	public String getStringId() {
		return this.stringId;
	}
	
	public static Joint byId(int id) {
		return JOINT_ENUM_BY_ID[id];
	}

	public static Joint byStringId(String stringId) {
		return JOINT_ENUM_BY_STRING_ID.get(stringId);
	}
}
