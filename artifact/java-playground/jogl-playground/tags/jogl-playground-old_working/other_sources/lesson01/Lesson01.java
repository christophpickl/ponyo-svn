package com.timelessname.lesson01;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Lesson01 implements GLEventListener {

	protected float pyramidRotation;
	protected float cubeRotation;
	
	public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glTranslatef(-1.5f,0.0f,-6.0f);
		gl.glRotatef(pyramidRotation,0.0f,1.0f,0.0f);
		drawPyramid(gl);
		pyramidRotation+=0.2f;
					
		gl.glLoadIdentity();
		gl.glTranslatef(1.5f,0.0f,-7.0f);				
		gl.glRotatef(cubeRotation,1.0f,1.0f,1.0f);			
		drawCube(gl);						
		cubeRotation-=0.15f;					
	}
	
	protected void drawPyramid(GL gl){
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
	
	protected void drawCube(GL gl){
		gl.glBegin(GL.GL_QUADS);					
			gl.glColor3f(0.0f,1.0f,0.0f);			
			gl.glVertex3f( 1.0f, 1.0f,-1.0f);			
			gl.glVertex3f(-1.0f, 1.0f,-1.0f);			
			gl.glVertex3f(-1.0f, 1.0f, 1.0f);			
			gl.glVertex3f( 1.0f, 1.0f, 1.0f);			
			gl.glColor3f(1.0f,0.5f,0.0f);			
			gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
			gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
			gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
			gl.glVertex3f( 1.0f,-1.0f,-1.0f);			
			gl.glColor3f(1.0f,0.0f,0.0f);			
			gl.glVertex3f( 1.0f, 1.0f, 1.0f);			
			gl.glVertex3f(-1.0f, 1.0f, 1.0f);			
			gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
			gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
			gl.glColor3f(1.0f,1.0f,0.0f);			
			gl.glVertex3f( 1.0f,-1.0f,-1.0f);			
			gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
			gl.glVertex3f(-1.0f, 1.0f,-1.0f);			
			gl.glVertex3f( 1.0f, 1.0f,-1.0f);			
			gl.glColor3f(0.0f,0.0f,1.0f);			
			gl.glVertex3f(-1.0f, 1.0f, 1.0f);			
			gl.glVertex3f(-1.0f, 1.0f,-1.0f);			
			gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
			gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
			gl.glColor3f(1.0f,0.0f,1.0f);			
			gl.glVertex3f( 1.0f, 1.0f,-1.0f);			
			gl.glVertex3f( 1.0f, 1.0f, 1.0f);			
			gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
			gl.glVertex3f( 1.0f,-1.0f,-1.0f);			
		gl.glEnd();	
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {

	}

	public void init(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL gl = drawable.getGL();
		final GLU glu = new GLU();
		
		gl.setSwapInterval(1);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(
				45.0f, 
				(double) width / (double) height, 
				0.1f,
				1000.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

}
