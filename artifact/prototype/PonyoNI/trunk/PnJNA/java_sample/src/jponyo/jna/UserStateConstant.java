package jponyo.jna;

public final class UserStateConstant {
	
	public static final int NEW_USER                         = 100;
	public static final int POSE_DETECTED                    = 200;
	public static final int CALIBRATION_STARTED              = 300;
	public static final int CALIBRATION_ENDED_SUCCESSFULLY   = 410;
	public static final int CALIBRATION_ENDED_UNSUCCESSFULLY = 420;
	public static final int LOST_USER                        = 999;

	private UserStateConstant() { /*enum*/ }
}
