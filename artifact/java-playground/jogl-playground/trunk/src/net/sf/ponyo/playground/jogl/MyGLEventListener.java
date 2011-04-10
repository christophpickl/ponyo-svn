package net.sf.ponyo.playground.jogl;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class MyGLEventListener implements GLEventListener {

	private static final float PYRAMID_ROTATION_VALUE = +0.2f;
//	private static final float CUBE_ROTATION_VALUE = -0.15f;
	private static final float CUBE_ROTATION_VALUE = -1.15f;
	
	private final ObjectDrawer drawer = new ObjectDrawer();
	private float pyramidRotation;
	private float cubeRotation;

	public void init(GLAutoDrawable drawable) {
		System.out.println("init GL");
		final GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}
	
	public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glTranslatef(-1.5f,0.0f,-6.0f);
		gl.glRotatef(this.pyramidRotation, 0.0f, 1.0f, 0.0f);
		this.drawer.drawPyramid(gl);
		this.pyramidRotation += PYRAMID_ROTATION_VALUE;
		
		gl.glLoadIdentity();
		gl.glTranslatef(1.5f,0.0f,-7.0f);				
		gl.glRotatef(this.cubeRotation, 1.0f, 1.0f, 1.0f);			
		this.drawer.drawCube(gl);						
		this.cubeRotation += CUBE_ROTATION_VALUE;					
	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// nothing to do
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println("reshape(drawable, x="+x+", y="+y+", width="+width+", height="+height+")");
		final GL gl = drawable.getGL();
		
		gl.setSwapInterval(1);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		final GLU glu = new GLU();
		glu.gluPerspective(
				45.0f, 
				(double) width / (double) height, 
				0.1f,
				1000.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

}
