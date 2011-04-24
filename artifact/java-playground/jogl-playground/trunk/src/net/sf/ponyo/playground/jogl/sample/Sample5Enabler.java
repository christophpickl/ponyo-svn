package net.sf.ponyo.playground.jogl.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import net.sf.ponyo.playground.jogl.sample.SampleInputs.BooleanInput;
import net.sf.ponyo.playground.jogl.sample.SampleInputs.RangeInput;

public class Sample5Enabler extends AbstractSample {
	
	private final BooleanInput inpCulling = new BooleanInput("Culling", false);
	private final BooleanInput inpDepth = new BooleanInput("Depth", true);
	private final BooleanInput inpOutline = new BooleanInput("Outline", false);
	private final RangeInput inpXRotate = new RangeInput("X-Rot", 0.0f, 180.0f, 0.0f);
	private final RangeInput inpYRotate = new RangeInput("Y-Rot", 0.0f, 180.0f, 30.0f);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(
		this.inpCulling, this.inpDepth, this.inpOutline, this.inpXRotate, this.inpYRotate
	));
	

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
//		GLfloat x,y,angle; int iPivot = 1;
		
		if(this.inpCulling.getValue().booleanValue()) gl.glEnable(GL.GL_CULL_FACE);
		else gl.glDisable(GL.GL_CULL_FACE);

		if(this.inpDepth.getValue().booleanValue()) gl.glEnable(GL.GL_DEPTH_TEST);
		else gl.glDisable(GL.GL_DEPTH_TEST);

		if(this.inpOutline.getValue().booleanValue()) gl.glPolygonMode(GL.GL_BACK, GL.GL_LINE);
		else gl.glPolygonMode(GL.GL_BACK, GL.GL_FILL);
		
		// save matrix state; do the rotation
		gl.glPushMatrix();
		
		
		this.drawStuff(gl);
		
		gl.glPopMatrix();
		// TODO swap buffers?!
	}

	private void drawStuff(GL gl) {
		gl.glRotatef(this.inpXRotate.getValue().floatValue(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(this.inpYRotate.getValue().floatValue(), 0.0f, 1.0f, 0.0f);
		
		gl.glBegin(GL.GL_TRIANGLE_FAN);
		// Pinnacle of cone is shared vertex for fan, moved up z-axis
		// to produce a cone instead of a circle
		gl.glVertex3f(0.0f, 0.0f, 75.0f);
		// Loop around in a circle and specify even points along the circle
		// as the vertices of the triangle fan
		int iPivot = 1;
		for(float angle = 0.0f; angle < (2.0f * Math.PI); angle += (Math.PI/8.0f)) {
			// Calculate x and y position of the next vertex
			float x = 50.0f * ((float) Math.sin(angle));
			float y = 50.0f * ((float) Math.cos(angle));
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
		for(float angle = 0.0f; angle < (2.0f*Math.PI); angle += (Math.PI/8.0f)) {
			// Calculate x and y position of the next vertex
			float x = 50.0f * ((float) Math.sin(angle));
			float y = 50.0f * ((float) Math.cos(angle));
			// Alternate color between red and green
			if((iPivot %2) == 0) gl.glColor3f(0.0f, 1.0f, 0.0f);
			else gl.glColor3f(1.0f, 0.0f, 0.0f);
			// Increment pivot to change color next time iPivot++;
			// Specify the next vertex for the triangle fan
			gl.glVertex2f(x, y);
		}
		// Done drawing the fan that covers the bottom
		gl.glEnd();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL.GL_FLAT);
		// Clockwise-wound polygons are front facing; this is reversed because we are using triangle fans
		gl.glFrontFace(GL.GL_CW);
	}

	@Override public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		SomeGL.setupCommonViewingVolume(drawable.getGL(), width, height);
	}

	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}
}
