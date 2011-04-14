package net.sf.ponyo.jponyo.connection.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;

/**
 * @since 0.1
 */
interface PnJNALibray extends Library {
	
	String LIB_NAME = "PnJNA";
	
	int pnStartByXmlConfig(String configPath, OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback);
	int pnStartByOniRecording(String oniPath, OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback);
	void pnShutdown();

	interface OnUserStateChangedCallback extends Callback {
		void onUserStateChanged(int userId, int userState);
	}
	
	interface OnJointPositionChangedCallback extends Callback {
		void onJointPositionChanged(int userId, int jointId, float x, float y, float z);
	}
}
