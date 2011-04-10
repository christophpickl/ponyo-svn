package jponyo.jna;

public enum UserState {

	NEW(UserStateConstant.NEW_USER, "new"),
	// FIXME implement me... UserState
	LOST(UserStateConstant.LOST_USER, "lost")
	;
	
	private final int id;
	public int id() { return this.id; }
	
	private final String label;
	public String label() { return this.label; }
	
	private final String eagerToString;
	@Override public String toString() { return this.eagerToString; }

	private UserState(int id, String label) {
		this.id = id;
		this.label = label;
		this.eagerToString = "UserState[label=" + label + ", id=" + id + "]";
	}
	
}
