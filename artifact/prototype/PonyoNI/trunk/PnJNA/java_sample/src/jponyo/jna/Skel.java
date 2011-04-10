package jponyo.jna;

public enum Skel {
	
	HEAD(0),
	NECK(1),
	TORSO(2),
//	WAIST(3),
	
//	LEFT_COLLAR(4),
	LEFT_SHOULDER(5),
	LEFT_ELBOW(6),
//	LEFT_WRIST(7),
	LEFT_HAND(8),
//	LEFT_FINGERTIP(9),
	
//	RIGHT_COLLAR(10),
	RIGHT_SHOULDER(11),
	RIGHT_ELBOW(12),
//	RIGHT_WRIST(13),
	RIGHT_HAND(14),
//	RIGHT_FINGERTIP(15),
	
	LEFT_HIP(16),
	LEFT_KNEE(17),
//	LEFT_ANKLE(18),
	LEFT_FOOT(19),
	
	RIGHT_HIP(20),
	RIGHT_KNEE(21),
//	RIGHT_ANKLE(22),
	RIGHT_FOOT(23)
	;
	
	public static final int COUNT = 24;
	
	private static Skel[] ALL = new Skel[COUNT];
	static {
		for(Skel skel : Skel.values()) {
			ALL[skel.id] = skel;
		}
	}
	
	private final int id;
	
	private Skel(int id) {
		this.id = id;
	}
	
	public static Skel byId(int id) {
		return ALL[id];
	}
	
	public int getId() {
		return this.id;
	}

}
