package jponyo.jna;

public interface PonyoNIListener {
	
	void onUserStateChanged(int userId, int userState);
	
	void onJointPositionChanged(int userId, int joint, float x, float y, float z);

	void onUpdateThreadThrewException(String exceptionMessage);
	
}
