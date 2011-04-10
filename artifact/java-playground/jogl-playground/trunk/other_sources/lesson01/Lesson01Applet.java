package com.timelessname.lesson01;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;

import com.sun.opengl.util.Animator;

public class Lesson01Applet extends Applet {
	
	private static final long serialVersionUID = 7403003329697278728L;
	
	protected GLCanvas canvas;
	protected Animator animator;
	
	public void init() {
		setLayout(new BorderLayout());
		GLCapabilities glCapabilities = new GLCapabilities();
		
		glCapabilities.setDoubleBuffered(true);
		glCapabilities.setHardwareAccelerated(true);

		canvas = new GLCanvas(glCapabilities);
		animator = new Animator(canvas);
		
		canvas.addGLEventListener(new Lesson01());
		
		add(canvas, BorderLayout.CENTER);
	}
	
	public void start() {
		animator.setRunAsFastAsPossible(false);
		animator.start();
	}

	public void stop() {
		animator.stop();
	}

}
