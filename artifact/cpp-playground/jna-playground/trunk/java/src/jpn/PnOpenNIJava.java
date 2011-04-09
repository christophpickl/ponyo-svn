package jpn;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class PnOpenNIJava {

    private interface PnOpenNICpp extends Library {
    	PnOpenNICpp INSTANCE = (PnOpenNICpp) Native.loadLibrary("PnOpenNICpp", PnOpenNICpp.class);
    	int pnGetNumber();
    }
    
    private static final PnOpenNICpp CPP = PnOpenNICpp.INSTANCE;
    
    public static void main(String[] args) {
    	System.out.println("main() START");
    	
		System.out.println("CPP says number=" + CPP.pnGetNumber());
		
		System.out.println("main() END");
	}
}
