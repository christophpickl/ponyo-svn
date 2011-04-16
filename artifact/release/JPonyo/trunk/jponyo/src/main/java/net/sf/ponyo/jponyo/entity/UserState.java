package net.sf.ponyo.jponyo.entity;

public enum UserState {
	
	NEW,
	CALIBRATION_STARTED,
	CALIBRATION_FAILED,
	TRACKING,
	// TODO PRE_LOST && (PRE_LOST_ABORTED == TRACKING)... shorter timeout
	LOST;

}
