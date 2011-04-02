package jniplay;


public class JavaApp {
	
	static {
		System.loadLibrary("jniPlayLib-0.1-SNAPSHOT");
	}
	
	public static void main(String[] args) {
		JavaApp app = new JavaApp();
		
		System.out.println("Calling native code ...");
		
//		app.sayHello();
		
		int result = app.nativeAdd(20, 22);
		System.out.println("result: " + result);
	}
	
//	private native void sayHello();

	private native int nativeAdd(int x, int y);
}