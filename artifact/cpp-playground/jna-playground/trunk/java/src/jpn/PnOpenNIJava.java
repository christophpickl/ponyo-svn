package jpn;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Callback.UncaughtExceptionHandler;
import com.sun.jna.ptr.IntByReference;

public class PnOpenNIJava {
	
	public static class PnReturnCodes {
		public static final int PN_RETURN_SUCCESS = 0;
		public static final int PN_RETURN_ERROR_UNKOWN = 1;
		private static final String[] RETURN_CODE_STRINGS = new String[] {
			"Success",
			"Error Unkown"
		};
		public static String getReturnCodeString(final int returnCode) {
			return RETURN_CODE_STRINGS[returnCode];
		}
	}
	
    private interface PnOpenNICpp extends Library {
    	
    	// TODO use UncaughtExceptionHandler
    	interface FoobarCallback extends Callback {
    		void onFoobar(int someNumber);
    	}
    	interface FoobazCallback extends Callback {
    		void onFoobazzz(int someNumber);
    	}
    	
    	PnOpenNICpp INSTANCE = (PnOpenNICpp) Native.loadLibrary("PnOpenNICpp", PnOpenNICpp.class);
    	
    	
    	int pnGetNumber(); // will invoke callback handler
    	
    	/*FoobarCallback old handler*/ void addCallbacks(FoobarCallback bar, FoobazCallback baz);
    	int errorSafeAdd2(IntByReference returnCode, int operand);
    	
//    	public static class JOYCAPSW extends Structure {
//    		public short wMid;
//    		public short wPid;
//    	}
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
			@Override public void onFoobar(int someNumber) {
				System.out.println("callback.onFoobar(someNumber=" + someNumber + ")");
//				throw new RuntimeException("haha!");
			}
    	};
    	PnOpenNICpp.FoobazCallback zallback = new PnOpenNICpp.FoobazCallback() {
			@Override public void onFoobazzz(int someNumber) {
				System.out.println("zallback.onFoobazzz(someNumber=" + someNumber + ")");
//				throw new RuntimeException("haha!");
			}
    	};
    	CPP.addCallbacks(callback, zallback);
		System.out.println("CPP says number=" + CPP.pnGetNumber());
		
		IntByReference returnCode = new IntByReference();
		final int result = CPP.errorSafeAdd2(returnCode, -10);
		System.out.println("CPP.errorSafeAdd2(..) = " + result);
		int returnCodeVal = returnCode.getValue();
		System.out.println("returnCode.getValue() = " + returnCodeVal + " -> [" + PnReturnCodes.getReturnCodeString(returnCodeVal) + "]");
		
		System.out.println("main() END");
	}
}
