package net.sf.ponyo.playground.jogl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.entity.Skeleton;
import net.sf.ponyo.jponyo.global.GlobalSpace;
import net.sf.ponyo.playground.jogl.glx.GLUtil;
import net.sf.ponyo.playground.jogl.glx.ObjectDrawer;

class MainWindowGL implements GLEventListener {

	private static final Color PRIMARY_COLOR_OFF = Color.GRAY;
	private static final float JOINT_SCALE = 0.3f;
	private static final float SKEL_DEF_Z = -10.0f;
	
	private final GlobalSpace data;
	
	public MainWindowGL(GlobalSpace data) {
		this.data = data;
	}
	
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
		Color primaryColor = this.data.isFooTracking ? null : PRIMARY_COLOR_OFF;
		
		drawSkel(gl, Joint.HEAD,  primaryColor,  0.0f, +3.5f, SKEL_DEF_Z);
		drawSkel(gl, Joint.NECK,  primaryColor,  0.0f, +2.0f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.TORSO, primaryColor,  0.0f, -0.5f, SKEL_DEF_Z);
//
//		drawSkel(gl, Skel.LEFT_SHOULDER,  primaryColor, -1.6f, +1.6f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.RIGHT_SHOULDER, primaryColor, +1.6f, +1.6f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.LEFT_ELBOW,     primaryColor, -2.6f, +0.5f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.RIGHT_ELBOW,    primaryColor, +2.6f, +0.5f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.LEFT_HAND,      primaryColor, -2.2f, -0.8f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.RIGHT_HAND,     primaryColor, +2.2f, -0.8f, SKEL_DEF_Z);
//
//		drawSkel(gl, Skel.LEFT_HIP,   primaryColor, -0.7f, -1.0f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.RIGHT_HIP,  primaryColor, +0.7f, -1.0f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.LEFT_KNEE,  primaryColor, -1.2f, -2.0f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.RIGHT_KNEE, primaryColor, +1.2f, -2.0f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.LEFT_FOOT,  primaryColor, -1.5f, -3.5f, SKEL_DEF_Z);
//		drawSkel(gl, Skel.RIGHT_FOOT, primaryColor, +1.5f, -3.5f, SKEL_DEF_Z);
	}
	
	private void drawSkel(GL gl, Joint joint, Color color, float defX, float defY, float defZ) {
		gl.glLoadIdentity();
		this.translateSkel(gl, joint, defX, defY, defZ);
		gl.glScalef(JOINT_SCALE, JOINT_SCALE, JOINT_SCALE);
		
		if(joint == Joint.HEAD) {
			ObjectDrawer.drawPyramid(gl, color);
		} else {
			ObjectDrawer.drawCube(gl, color);
		}
	}
	
	private void translateSkel(GL gl, Joint joint, float defX, float defY, float defZ) {
		if(this.data.isFooTracking == false) {
			gl.glTranslatef(defX, defY, defZ);
		} else {
			GLUtil.translate(gl, this.data.getUsers().iterator().next().getSkeleton(), joint);
		}
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
