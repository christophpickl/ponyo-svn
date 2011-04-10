package net.sf.ponyo.playground.jogl;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.opengl.util.Animator;

// sample using jogl-1.1.1-rc8 (needs 32bit jvm)
//
// * jogl needs some jars (jogl, gluegen-rt) and some jnilibs (jogl, jogl_awt, jogl_cg, gluegen-rt)
// * if UnsatisfiedLinkError "no jogl in java.library.path":
//   ==> JVM ARG += "-Djava.library.path=path/to/jnilib/folder" ||
//	                (NOO!! System.setProperty("java.library.path", "/var/folder/bla/foo/bar/") ||)
//	                put jnilibs in system extension folder
// * if UnsatisfiedLinkError "no matching architecture in universal wrapper":
//   ==> JVM ARG += "-d32" // switch JVM to 32bit mode
public class App {
	
	private static final long serialVersionUID = 7633042051769682994L;
	
	public static void main(String[] args) {
		final GLCanvas canvas = new GLCanvas();
	    canvas.addGLEventListener(new MyGLEventListener());
	    final Animator animator = new Animator(canvas);
	    
	    final JFrame frame = new JFrame("Jogl Playground");
	    frame.add(canvas);
	    frame.setSize(800, 600);
	    
	    frame.addWindowListener(new WindowAdapter() {
	        @Override public void windowClosing(WindowEvent e) {
	        	System.out.println("windowClosing()");
	        	new Thread(new Runnable() {
	        		public void run() {
	        			animator.stop();
	        			System.exit(0);
	        		}
	        	}).start();
	        }
	    });
	    SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				System.out.println("displaying window");
				frame.setVisible(true);
			    animator.start();
			}
		});
	}
}
