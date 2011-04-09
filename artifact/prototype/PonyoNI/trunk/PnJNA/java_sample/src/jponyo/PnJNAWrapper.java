package jponyo;

import com.sun.jna.Native;

public class PnJNAWrapper {
	
	private static final String LIB_NAME = "PnJNA";
	
	private static PnJNA instance;
    
    public void initLib() {
    	System.out.println("PnJNAWrapper.initLib()");
    	if(PnJNAWrapper.instance == null) {
    		PnJNAWrapper.instance = (PnJNA) Native.loadLibrary(LIB_NAME, PnJNA.class);
    	}
    }

    public void startup() {
    	System.out.println("PnJNAWrapper.startup()");
    	PnJNAWrapper.instance.startup();
    }

    public void shutdown() {
    	System.out.println("PnJNAWrapper.shutdown()");
    	PnJNAWrapper.instance.shutdown();
    }

}
