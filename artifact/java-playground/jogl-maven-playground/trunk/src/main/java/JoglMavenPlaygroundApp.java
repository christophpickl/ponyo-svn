
import javax.media.opengl.GL;

public class JoglMavenPlaygroundApp {

	
	public static void main(String[] args) {
		System.out.println("main() START");
//		System.out.println("ponyo.java.library.path: [" + System.getProperty("ponyo.java.library.path") + "]");
		// see: http://www.google.com/codesearch/p?hl=en#YRfjEilsrqU/trunk/java/com/almworks/sqlite4java/Internal.java&q=sqlite4java.library.path%20package:http://sqlite4java\.googlecode\.com&d=5
		// String libFile = System.mapLibraryName(libname);
//		System.out.println(getClassUrl(App.class));
		
//		String nativeLibsPath = new File("target/ponyo_native_libs").getAbsolutePath(); // TODO quick hack
//		String joglLibPath = "jar:file:" + nativeLibsPath + "/jogl-macosx-universal-1.1.1-rc6.jar!libjogl.jnilib";
//		System.out.println("joglLibPath: ["+joglLibPath+"]");
//		System.load(joglLibPath);
		
//			NativeLoader.loadLibrary("jogl"); // => META-INF/lib/libjogl.dylib on classpath
		
//		System.setProperty("java.library.path", System.getProperty("java.library.path") + ":/Users/phudy/_dev/josc/jponyo-reactor/admin-console/target/foolib");
		System.out.println("java.library.path: [" + System.getProperty("java.library.path") + "]");
		
		GL gl;
		
		System.out.println("main() END");
	}

}
