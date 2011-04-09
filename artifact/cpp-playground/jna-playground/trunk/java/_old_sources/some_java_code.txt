package pnj;

import com.sun.jna.Library;
import com.sun.jna.Native;

// http://today.java.net/pub/a/today/2009/05/19/protect-your-legacy-code-jna.html
// http://jna.java.net/#getting_started
public class Japp {

//    public interface PonyoLibrary extends Library {
//    	PonyoLibrary INSTANCE = (PonyoLibrary) Native.loadLibrary("PnJnaWrap", PonyoLibrary.class);
//    	void foo();
//    	int addi(int x, int y);
//    }
    public interface PonyoCppLibrary extends Library {
    	PonyoCppLibrary INSTANCE = (PonyoCppLibrary) Native.loadLibrary("PnJnaCpp", PonyoCppLibrary.class);
    	void init();
    	void shutdown();
    	int countDevices();
    }
//	  public interface PnniLibrary extends Library {
//		  PnniLibrary INSTANCE = (PnniLibrary) Native.loadLibrary("pnni", PnniLibrary.class);
//		  void fooc();
//		  int getOpenniVersion();
//	  }
//	  public interface OpenniLibrary extends Library {
//		  OpenniLibrary INSTANCE = (OpenniLibrary) Native.loadLibrary("OpenNI", OpenniLibrary.class);
//		  
//		  // XN_C_API XnStatus xnGetVersion(XnVersion* pVersion)
//		  /*
//			typedef struct XnVersion
//			{
//				XnUInt8 nMajor;
//				XnUInt8 nMinor;
//				XnUInt16 nMaintenance;
//				XnUInt32 nBuild;
//			} XnVersion;
//		   */
////		  int xnGetVersion(com.sun.jna.Pointer p);
//	  }
    public static void main(String[] args) {
    	System.out.println("J main START");
    	
    	final PonyoCppLibrary lib = PonyoCppLibrary.INSTANCE;
    	
    	System.out.println("J init");
    	lib.init();
    	
    	System.out.println("J lib.countDevices()=");
    	System.out.println("J ... result: " + lib.countDevices());
    	
    	System.out.println("J shutdown");
    	lib.shutdown();
    	
//    	System.out.println(PonyoLibrary.INSTANCE.addi(20, 22));
//    	PonyoLibrary.INSTANCE.foo();
//    	PnniLibrary.INSTANCE.fooc();
//    	System.out.println("OpenNI verison: "+ PnniLibrary.INSTANCE.getOpenniVersion());
//    	XnVersion.ByReference r = new XnVersion.ByReference();
//    	int rc = OpenniLibrary.INSTANCE.xnGetVersion(r);
//    	System.out.println("rc: " + rc);
//    	
    	System.out.println("J main END");
    }
}
