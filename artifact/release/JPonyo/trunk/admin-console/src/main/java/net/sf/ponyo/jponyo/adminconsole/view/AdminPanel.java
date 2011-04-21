package net.sf.ponyo.jponyo.adminconsole.view;

import java.awt.BorderLayout;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;

import com.sun.opengl.util.Animator;

public class AdminPanel extends JPanel implements ContinuousUserListener {

	private static final long serialVersionUID = 2357164011556061774L;

	private final Animator animator;
	private final SkeletonGLRenderer skeletonGLRenderer = new SkeletonGLRenderer();

	public AdminPanel() {
		super(new BorderLayout());

		GLCapabilities glCapabilities = new GLCapabilities();
		glCapabilities.setDoubleBuffered(true);
		glCapabilities.setHardwareAccelerated(true);
		
		GLCanvas canvas = new GLCanvas(glCapabilities);
		
		canvas.addGLEventListener(this.skeletonGLRenderer);
		this.animator = new Animator(canvas);
		
		this.add(canvas, BorderLayout.CENTER);
	}

	public void start() {
		this.animator.start();
	}
	
	public void stop() {
		this.animator.stop();
	}

	public void onCurrentUserChanged(User user) {
		this.skeletonGLRenderer.setUser(user);
	}
	
}
