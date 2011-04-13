package net.sf.ponyo.jponyo.connector.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;

interface PnJNALibray extends Library {
	
	String LIB_NAME = "PnJNA";
	
	int pnStartWithXml(String configPath, OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback);
	int pnStartRecording(String oniPath, OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback);
	void pnDestroy();

	interface OnUserStateChangedCallback extends Callback {
		void onUserStateChanged(int userId, int userState);
	}
	
	interface OnJointPositionChangedCallback extends Callback {
		void onJointPositionChanged(int userId, int joint, float x, float y, float z);
	}
}
