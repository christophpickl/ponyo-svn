package net.sf.ponyo.playground.jogl.sample;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

public class Sample2Pyramide extends AbstractSample {

	@Override public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH); // enable smooth color transitions ... usually this is the default, but just to get sure
//		gl.glShadeModel(GL.GL_FLAT);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // black bg
		gl.glClearDepth(1.0f);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}
	
	@Override public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glTranslatef(-2.0f, 0.0f, -7.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f); // let it show to us
			gl.glRotatef(-20.0f, 0.0f, 0.0f, 1.0f); // and a little bit to the right
			drawRainbowPyramide(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glTranslatef(2.0f, 0.0f, -7.0f);
			drawRainbowPyramide(gl);
		gl.glPopMatrix();
	}

	private void drawRainbowPyramide(final GL gl) {
		gl.glBegin(GL.GL_TRIANGLES);					
			gl.glColor3f(1.0f,0.0f,0.0f);			
			gl.glVertex3f( 0.0f, 1.0f, 0.0f);			
			gl.glColor3f(0.0f,1.0f,0.0f);			
			gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
			gl.glColor3f(0.0f,0.0f,1.0f);			
			gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
			gl.glColor3f(1.0f,0.0f,0.0f);			
			gl.glVertex3f( 0.0f, 1.0f, 0.0f);			
			gl.glColor3f(0.0f,0.0f,1.0f);			
			gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
			gl.glColor3f(0.0f,1.0f,0.0f);			
			gl.glVertex3f( 1.0f,-1.0f, -1.0f);			
			gl.glColor3f(1.0f,0.0f,0.0f);			
			gl.glVertex3f( 0.0f, 1.0f, 0.0f);			
			gl.glColor3f(0.0f,1.0f,0.0f);			
			gl.glVertex3f( 1.0f,-1.0f, -1.0f);			
			gl.glColor3f(0.0f,0.0f,1.0f);			
			gl.glVertex3f(-1.0f,-1.0f, -1.0f);			
			gl.glColor3f(1.0f,0.0f,0.0f);			
			gl.glVertex3f( 0.0f, 1.0f, 0.0f);			
			gl.glColor3f(0.0f,0.0f,1.0f);			
			gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
			gl.glColor3f(0.0f,1.0f,0.0f);			
			gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
		gl.glEnd();
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
		// nothing to do
	}

}
