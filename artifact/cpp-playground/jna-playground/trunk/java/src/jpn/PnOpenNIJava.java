package jpn;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Callback.UncaughtExceptionHandler;

public class PnOpenNIJava {

    private interface PnOpenNICpp extends Library {
    	
    	// TODO use UncaughtExceptionHandler
    	interface FoobarCallback extends Callback {
    		void invoke(int someNumber);
    	}
    	
    	PnOpenNICpp INSTANCE = (PnOpenNICpp) Native.loadLibrary("PnOpenNICpp", PnOpenNICpp.class);
    	
    	int pnGetNumber(); // will invoke callback handler
    	/*FoobarCallback old handler*/ void addCallback(FoobarCallback fn);
    }
    
    private static final PnOpenNICpp CPP = PnOpenNICpp.INSTANCE;
    
    public static void main(String[] args) {
    	System.out.println("main() START");
    	Native.setCallbackExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Callback callback, Throwable throwable) {
				System.err.println("uncaughtException(callback="+callback+", throwable="+throwable+")");
				throwable.printStackTrace();
			}
		});
    	
    	PnOpenNICpp.FoobarCallback callback = new PnOpenNICpp.FoobarCallback() {
			@Override public void invoke(int someNumber) {
				System.out.println("callback.invoke(someNumber=" + someNumber + ")");
//				throw new RuntimeException("haha!");
			}
    	};
    	CPP.addCallback(callback);
		System.out.println("CPP says number=" + CPP.pnGetNumber());
		
		System.out.println("main() END");
	}
}
