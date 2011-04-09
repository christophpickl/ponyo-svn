package jponyo;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Callback.UncaughtExceptionHandler;
import com.sun.jna.ptr.IntByReference;

public class PnJNAWrapper {
	
	private static final String LIB_NAME = "PnJNA";
	
	private static PnJNA instance;
	
    public static void main(String[] args) {
    	PnJNAWrapper jna = new PnJNAWrapper();
    	jna.init();
    	System.out.println("jna.getNumber() = " + jna.getNumber());
	}
    
    public void init() {
    	if(PnJNAWrapper.instance != null) {
    		// warn
    		return;
//    		throw new RuntimeException("Singleton!");
    	}
    	PnJNAWrapper.instance = (PnJNA) Native.loadLibrary(LIB_NAME, PnJNA.class);
    }
    
    public int getNumber() {
    	return PnJNAWrapper.instance.pnGetNumber();
    }

    private interface PnJNA extends Library {
    	int pnGetNumber();
    }
}
