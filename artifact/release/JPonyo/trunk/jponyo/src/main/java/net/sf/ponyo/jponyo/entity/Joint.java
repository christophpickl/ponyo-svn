package net.sf.ponyo.jponyo.entity;

/**
 * @since 0.1
 */
public enum Joint {

	HEAD(0, "Head"),
	NECK(1, "Neck");
	
	public static final int MAX_JOINT_ID = 1;
	
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
}
