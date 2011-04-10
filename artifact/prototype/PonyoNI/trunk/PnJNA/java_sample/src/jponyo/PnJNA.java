package jponyo;

import com.sun.jna.Callback;
import com.sun.jna.Library;

public interface PnJNA extends Library {
	void startup(OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback);
	void shutdown();

	interface OnUserStateChangedCallback extends Callback {
		void invoke(int userId, int userState);
	}

	interface OnJointPositionChangedCallback extends Callback {
		void invoke(int userId, int joint, float x, float y, float z);
	}
}
