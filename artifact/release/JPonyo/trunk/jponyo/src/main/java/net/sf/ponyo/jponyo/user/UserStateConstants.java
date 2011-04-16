package net.sf.ponyo.jponyo.user;

/**
 * @since 0.1
 */
class UserStateConstants {

	static final int ID_NEW = 0;
	static final int ID_CALIBRATION_STARTED = 1;
	static final int ID_CALIBRATION_FAILED = 2;
	static final int ID_TRACKING = 3;
	static final int ID_LOST = 4;
	
	static final UserState[] MAP_ID_TO_ENUM = new UserState[5];
	static {
		MAP_ID_TO_ENUM[ID_NEW] = UserState.NEW;
		MAP_ID_TO_ENUM[ID_CALIBRATION_STARTED] = UserState.CALIBRATION_STARTED;
		MAP_ID_TO_ENUM[ID_CALIBRATION_FAILED] = UserState.CALIBRATION_FAILED;
		MAP_ID_TO_ENUM[ID_TRACKING] = UserState.TRACKING;
		MAP_ID_TO_ENUM[ID_LOST] = UserState.LOST;
	}
}
