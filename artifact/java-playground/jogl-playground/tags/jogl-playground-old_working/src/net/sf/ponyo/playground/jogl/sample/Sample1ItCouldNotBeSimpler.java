package net.sf.ponyo.playground.jogl.sample;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import net.sf.ponyo.playground.jogl.sample.SampleStarter.SimpleSample;

public class Sample1ItCouldNotBeSimpler extends SimpleSample {

	
	@Override public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // black background color
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}
	
	@Override public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glColor3f(1.0f, 0.0f, 0.0f); // red foreground color
		gl.glLoadIdentity(); // reset
		gl.glTranslatef(1.5f, 0.0f, -6.0f);
		{
			gl.glBegin(GL.GL_QUADS);
			//               X      Y      Z
			gl.glVertex3f(-1.0f, +1.0f,  0.0f); // left top
			gl.glVertex3f(+1.0f, +1.0f,  0.0f); // right top
			gl.glVertex3f(+1.0f, -1.0f,  0.0f); // right bottom
			gl.glVertex3f(-1.0f, -1.0f,  0.0f); // left bottom
			gl.glEnd();
		}
	}

	@Override public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println("reshape()");
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
