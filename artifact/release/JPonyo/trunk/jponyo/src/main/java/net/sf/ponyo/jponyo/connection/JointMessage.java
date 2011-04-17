package net.sf.ponyo.jponyo.connection;

public class JointMessage {
	
	private final int userId;
	
	private final JointData data;

	public JointMessage(int userId, JointData data) {
		this.userId = userId;
		this.data = data;
	}

	public int getUserId() {
		return this.userId;
	}

	public JointData getData() {
		return this.data;
	}
	
	@Override
	public String toString() {
		return "JointMessage[userId=" + this.userId + ", data=" + this.data + "]";
	}
	
}
