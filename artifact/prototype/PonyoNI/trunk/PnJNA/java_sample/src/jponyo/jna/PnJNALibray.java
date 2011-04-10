package jponyo.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;

interface PnJNALibray extends Library {

	String LIB_NAME = "PnJNA";
	
	void start(
		OnUserStateChangedCallback userCallback,
		OnJointPositionChangedCallback jointCallback,
		OnUpdateThreadThrewExceptionCallback threadExceptionCallback
	);
	
	void startRecording(
		OnUserStateChangedCallback userCallback,
		OnJointPositionChangedCallback jointCallback,
		OnUpdateThreadThrewExceptionCallback threadExceptionCallback,
		String oniFilePath
	);
	
	void shutdown();
	
	
	interface OnUserStateChangedCallback extends Callback {
		void onUserStateChanged(int userId, int userState);
	}

	interface OnJointPositionChangedCallback extends Callback {
		void onJointPositionChanged(int userId, int joint, float x, float y, float z);
	}

	interface OnUpdateThreadThrewExceptionCallback extends Callback {
		void onUpdateThreadThrewExceptionCallback(String exceptionMessage);
	}
	
}
