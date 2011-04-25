package net.sf.ponyo.playground.jogl.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;

import com.sun.opengl.util.GLUT;

import net.sf.ponyo.playground.jogl.glx.GLUtil;
import net.sf.ponyo.playground.jogl.sample.SampleInputs.BooleanInput;
import net.sf.ponyo.playground.jogl.sample.SampleInputs.RangeInput;

public class Sample5Enabler extends AbstractSample {

	private float inpLeftRight = 0.0f;
	private float inpUpDown = 0.0f;
	private final BooleanInput inpCulling = new BooleanInput("Cull Face", false);
	private final BooleanInput inpDepth = new BooleanInput("Depth Test", true);
	private final BooleanInput inpOutline = new BooleanInput("Back Line/Fill", false);
	private final RangeInput inpXRotate = new RangeInput("X-Rot", 0.0f, 180.0f, 120.0f);
	private final RangeInput inpYRotate = new RangeInput("Y-Rot", 0.0f, 180.0f, 30.0f);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(
		this.inpCulling, this.inpDepth, this.inpOutline, this.inpXRotate, this.inpYRotate
	));

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		gl.glShadeModel(GL.GL_FLAT);
//		gl.glShadeModel(GL.GL_SMOOTH); ... especially useful if defining different colors on a polygon
		
		// Clockwise-wound polygons are front facing; this is reversed because we are using triangle fans
		gl.glFrontFace(GL.GL_CW);
	}

	@Override
	public void initGL(GLCapabilities capabilities) {
		capabilities.setDoubleBuffered(true); // here we enable double buffering!
		capabilities.setHardwareAccelerated(true);
//		capabilities.setStencilBits(arg0) // TODO enable stencil buffer?
//		capabilities.setDepthBits(arg0) // TODO enable depth buffer?
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		if(this.inpCulling.getValue().booleanValue())
			gl.glEnable(GL.GL_CULL_FACE);
		else
			gl.glDisable(GL.GL_CULL_FACE);

		if(this.inpDepth.getValue().booleanValue())
			gl.glEnable(GL.GL_DEPTH_TEST);
		else
			gl.glDisable(GL.GL_DEPTH_TEST);

		if(this.inpOutline.getValue().booleanValue())
			gl.glPolygonMode(GL.GL_BACK, GL.GL_LINE);
		else
			gl.glPolygonMode(GL.GL_BACK, GL.GL_FILL);
		
		// save matrix state; do the rotation
		gl.glPushMatrix();
		gl.glRotatef(this.inpXRotate.getValue().floatValue(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(this.inpYRotate.getValue().floatValue(), 0.0f, 1.0f, 0.0f);
		
		this.drawStuff(gl);
		
//		GLUT glut = new GLUT();
//		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
//		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
//		glut.glutSolidSphere(50.0, 15, 15);
		
		gl.glPopMatrix();
		// swap buffers done in SampleStarter via: capabilities.setDoubleBuffered
	}
	
	private void drawStuff(GL gl) {
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		// Pinnacle of cone is shared vertex for fan, moved up z-axis to produce a cone instead of a circle
		gl.glVertex3f(0.0f, 0.0f, 75.0f);
		// Loop around in a circle and specify even points along the circle as the vertices of the triangle fan
		int iPivot = 1;
		for(float angle = 0.0f; angle < (2.0f * Math.PI) + 0.000001f; angle += (Math.PI/8.0f)) { // TODO last segment hack
			// Calculate x and y position of the next vertex
			float x = 50.0f * GLUtil.sinf(angle) + this.inpLeftRight;
			float y = 50.0f * GLUtil.cosf(angle) + this.inpUpDown;
			// Alternate color between red and green
			if((iPivot %2) == 0) gl.glColor3f(0.0f, 1.0f, 0.0f);
			else gl.glColor3f(1.0f, 0.0f, 0.0f);
			// Increment pivot to change color next time
			iPivot++;
			// Specify the next vertex for the triangle fan
			gl.glVertex2f(x, y);
		}
		gl.glEnd();
		
		// Begin a new triangle fan to cover the bottom
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		// Center of fan is at the origin
		gl.glVertex2f(0.0f, 0.0f);
		for(float angle = 0.0f; angle < (2.0f*Math.PI) + 0.000001f; angle += (Math.PI/8.0f)) {
			// Calculate x and y position of the next vertex
			float x = 50.0f * GLUtil.sinf(angle);
			float y = 50.0f * GLUtil.cosf(angle);
			// Alternate color between red and green
			if((iPivot %2) == 0) gl.glColor3f(0.0f, 1.0f, 0.0f);
			else gl.glColor3f(1.0f, 0.0f, 0.0f);
			// Increment pivot to change color next time
			iPivot++;
			// Specify the next vertex for the triangle fan
			gl.glVertex2f(x, y);
		}
		// Done drawing the fan that covers the bottom
		gl.glEnd();
	}

	@Override public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		SomeGL.setupCommonViewingVolume(drawable.getGL(), width, height);
	}

	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}
	@Override public void setKeyLeftPressed(boolean pressed) {
		this.inpLeftRight -= 2.0f;
	}
	@Override public void setKeyRightPressed(boolean pressed) {
		this.inpLeftRight += 2.0f;
	}
	@Override public void setKeyUpPressed(boolean pressed) {
		this.inpUpDown -= 2.0f;
	}
	@Override public void setKeyDownPressed(boolean pressed) {
		this.inpUpDown += 2.0f;
	}
}
