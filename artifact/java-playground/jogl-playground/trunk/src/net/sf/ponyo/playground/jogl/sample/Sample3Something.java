package net.sf.ponyo.playground.jogl.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import net.sf.ponyo.playground.jogl.glx.ObjectDrawer;
import net.sf.ponyo.playground.jogl.sample.SampleInputs.RangeInput;

public class Sample3Something extends AbstractSample {

	private final RangeInput inpTranslateX = new RangeInput("Translate-X", -2.0f, 2.0f, 0.0f);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(this.inpTranslateX));
	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}

	@Override public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // black bg
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}
	
	@Override public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glTranslatef(this.inpTranslateX.getValue().floatValue(), 1.0f, -7.0f);
		ObjectDrawer.drawCube(gl);
	}

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

	@Override public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// empty
	}

}
