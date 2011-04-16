package net.sf.ponyo.jponyo.user;

/**
 * @since 0.1
 */
public enum UserState {
	
	NEW,
	CALIBRATION_STARTED,
	CALIBRATION_FAILED,
	TRACKING,
	// TODO PRE_LOST && (PRE_LOST_ABORTED == TRACKING)... shorter timeout
	LOST;
	
}
