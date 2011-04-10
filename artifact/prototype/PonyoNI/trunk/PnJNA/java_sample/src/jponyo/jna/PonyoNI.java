package jponyo.jna;

import java.util.HashSet;
import java.util.Set;

import jponyo.jna.PnJNALibray.OnJointPositionChangedCallback;
import jponyo.jna.PnJNALibray.OnUpdateThreadThrewExceptionCallback;
import jponyo.jna.PnJNALibray.OnUserStateChangedCallback;

import com.sun.jna.Native;

public class PonyoNI {
	
	private static PnJNALibray instance;
	
	final Set<PonyoNIListener> listeners = new HashSet<PonyoNIListener>();
	
	private final OnUserStateChangedCallback userCallback = new OnUserStateChangedCallback() {
		@Override public void onUserStateChanged(int userId, int userState) {
			for (final PonyoNIListener listener : PonyoNI.this.listeners) {
				listener.onUserStateChanged(userId, userState);
			}
		}
	};
	
	private final OnJointPositionChangedCallback jointCallback = new OnJointPositionChangedCallback() {
		@Override public void onJointPositionChanged(int userId, int joint, float x, float y, float z) {
			for (final PonyoNIListener listener : PonyoNI.this.listeners) {
				listener.onJointPositionChanged(userId, joint, x, y, z);
			}
		}
	};
	
	private final OnUpdateThreadThrewExceptionCallback threadExceptionCallback = new OnUpdateThreadThrewExceptionCallback() {
		@Override public void onUpdateThreadThrewExceptionCallback(String exceptionMessage) {
			for (final PonyoNIListener listener : PonyoNI.this.listeners) {
				listener.onUpdateThreadThrewException(exceptionMessage);
			}
		}
	};
	
    public void initLib() {
    	System.out.println("PnJNAWrapper.initLib()");
    	if(PonyoNI.instance == null) {
    		PonyoNI.instance = (PnJNALibray) Native.loadLibrary(PnJNALibray.LIB_NAME, PnJNALibray.class);
    	}
    }

    public void start() {
    	System.out.println("PnJNAWrapper.startup()");
    	PonyoNI.instance.start(this.userCallback, this.jointCallback, this.threadExceptionCallback);
    }
    
    public void startRecording(String oniFilePath) {
    	System.out.println("PnJNAWrapper.startRecording(oniFilePath=" + oniFilePath + ")");
    	PonyoNI.instance.startRecording(this.userCallback, this.jointCallback, this.threadExceptionCallback, oniFilePath);
    }
    
    public void shutdown() {
    	System.out.println("PnJNAWrapper.shutdown()");
    	PonyoNI.instance.shutdown();
    }

    public void addListener(PonyoNIListener listener) {
    	System.out.println("PnJNAWrapper.addListener(listener=" + listener + ")");
    	this.listeners.add(listener);
    }
}
