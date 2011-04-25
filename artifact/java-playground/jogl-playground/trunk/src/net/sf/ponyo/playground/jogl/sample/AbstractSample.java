package net.sf.ponyo.playground.jogl.sample;

import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.glu.GLU;

public abstract class AbstractSample implements Sample {
	
	private boolean keyLeftPressed;
	private boolean keyRightPressed;
	private boolean keyUpPressed;
	private boolean keyDownPressed;
	

	// default settings
	@Override public void initGL(GLCapabilities capabilities) {
		// here we enable double buffering!
		capabilities.setDoubleBuffered(true);
		capabilities.setHardwareAccelerated(true);
	}
	
	@Override
	public Collection<SampleInput> getInputs() {
		return null;
	}

	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// empty
	}

	@Override public void setKeyLeftPressed(boolean pressed) {
		this.keyLeftPressed = pressed;
	}
	protected final boolean isKeyLeftPressed() { return this.keyLeftPressed; }
	@Override public void setKeyRightPressed(boolean pressed) {
		this.keyRightPressed = pressed;
	}
	protected final boolean isKeyRightPressed() { return this.keyRightPressed; }
	@Override public void setKeyUpPressed(boolean pressed) {
		this.keyUpPressed = pressed;
	}
	protected final boolean isKeyUpPressed() { return this.keyUpPressed; }
	@Override public void setKeyDownPressed(boolean pressed) {
		this.keyDownPressed = pressed;
	}
	protected final boolean isKeyDownPressed() { return this.keyDownPressed; }
	

	@Override public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();
		gl.setSwapInterval(1);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU glu = new GLU();
		glu.gluPerspective(45.0f, (double) width / (double) height, 0.1f, 1000.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
