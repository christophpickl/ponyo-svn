package com.timelessname.lesson01;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;

import com.sun.opengl.util.Animator;

public class Lesson01Window extends Frame {
	
	private static final long serialVersionUID = 7633042051769682994L;
	
	protected GLCanvas canvas;
	protected Animator animator;
	
	public static void main(String[] args) {
		Frame frame = new Frame("Lesson01");
	    GLCanvas canvas = new GLCanvas();

	    canvas.addGLEventListener(new Lesson01());
	    frame.add(canvas);
	    frame.setSize(800, 600);
	    final Animator animator = new Animator(canvas);
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          new Thread(new Runnable() {
	              public void run() {
	                animator.stop();
	                System.exit(0);
	              }
	            }).start();
	        }
	      });
	    frame.setVisible(true);
	    animator.start();
	}
}
