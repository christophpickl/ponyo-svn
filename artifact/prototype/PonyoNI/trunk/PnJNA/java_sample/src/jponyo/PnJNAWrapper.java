package jponyo;

import java.util.HashSet;
import java.util.Set;

import jponyo.PnJNA.OnJointPositionChangedCallback;
import jponyo.PnJNA.OnUserStateChangedCallback;

import com.sun.jna.Native;

public class PnJNAWrapper implements OnUserStateChangedCallback, OnJointPositionChangedCallback {
	
	private static final String LIB_NAME = "PnJNA";
	
	private static PnJNA instance;
	
	private final Set<PnJNAWrapperListener> listeners = new HashSet<PnJNAWrapperListener>();
	
    public void initLib() {
    	System.out.println("PnJNAWrapper.initLib()");
    	if(PnJNAWrapper.instance == null) {
    		PnJNAWrapper.instance = (PnJNA) Native.loadLibrary(LIB_NAME, PnJNA.class);
    	}
    }

    public void startup() {
    	System.out.println("PnJNAWrapper.startup()");
    	PnJNAWrapper.instance.startup(this, this);
    }
    
    public void shutdown() {
    	System.out.println("PnJNAWrapper.shutdown()");
    	PnJNAWrapper.instance.shutdown();
    }

	@Override public void invoke(int userId, int userState) {
		for (final PnJNAWrapperListener listener : this.listeners) {
			listener.onUserStateChanged(userId, userState);
		}
	}

	@Override public void invoke(int userId, int joint, float x, float y, float z) {
		for (final PnJNAWrapperListener listener : this.listeners) {
			listener.onJointPositionChanged(userId, joint, x, y, z);
		}
	}
    
    public void addListener(PnJNAWrapperListener listener) {
    	System.out.println("PnJNAWrapper.addListener(listener=" + listener + ")");
    	this.listeners.add(listener);
    }

    
	public interface PnJNAWrapperListener {
		void onUserStateChanged(int userId, int userState);
		void onJointPositionChanged(int userId, int joint, float x, float y, float z);
	}
}
