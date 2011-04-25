package net.sf.ponyo.playground.jogl.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import net.sf.ponyo.playground.jogl.sample.SampleInputs.BooleanInput;
import net.sf.ponyo.playground.jogl.sample.SampleInputs.RangeInput;


public class Sample4Points extends AbstractSample {

	private final BooleanInput inpPoints = new BooleanInput("Points", false);
	private final BooleanInput inpLines = new BooleanInput("Lines", false);
	private final RangeInput inpXRotate = new RangeInput("X-Rot", 0.0f, 180.0f, 0.0f);
	private final RangeInput inpYRotate = new RangeInput("Y-Rot", 0.0f, 180.0f, 0.0f);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(
		this.inpPoints, this.inpLines, this.inpXRotate, this.inpYRotate
	));

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		// Save matrix state and do the rotation
		gl.glPushMatrix();
		gl.glRotatef(this.inpXRotate.getValue().floatValue(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(this.inpYRotate.getValue().floatValue(), 0.0f, 1.0f, 0.0f);
		
		if(this.inpPoints.getValue() == Boolean.TRUE) this.drawPoints(gl);
		if(this.inpLines.getValue() == Boolean.TRUE) this.drawSimpleLines(gl);
		this.drawLineStrips(gl);
		
		gl.glPopMatrix(); // Restore transformations
//		TODO gl.glutSwapBuffers(); // flush and swap?
	}
	
	private void drawLineStrips(GL gl) {
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		float z = -50.0f;
		for(float angle = 0.0f; angle <= (2.0f * Math.PI) * 3.0f; angle += 0.1f) {
			float x = 50.0f * ((float) Math.sin(angle));
			float y = 50.0f * ((float) Math.cos(angle));
			gl.glVertex3f(x, y, z);
			z += 0.5f;
		}
		gl.glEnd();
	}

	private void drawPoints(GL gl) {
		float currentSize = 1.0f;
		float z = -50.0f;
		float colR = 0.0f;
		float colG = 0.0f;
		float colB = 1.0f;
//		gl.glColor3f(1.0f, 0.0f, 0.0f);
		for(float angle = 0.0f; angle <= (2.0f * Math.PI) * 3.0f; angle += 0.05f) {
			gl.glPointSize(2.0f); // must be outside of glBegin/End
//			gl.glPointSize(currentSize); // must be outside of glBegin/End
			currentSize += 0.125f; // hardcoded step size
			float x = 50.0f * ((float) Math.sin(angle));
			float y = 50.0f * ((float) Math.cos(angle));
			
			colG += 0.02f;
			colB -= 0.01f;
			
			gl.glBegin(GL.GL_POINTS);
				gl.glColor4f(colR, colG, colB, 1.0f);
				gl.glVertex3f(x, y, z);
			gl.glEnd();

			colR += 0.01f;
			z += 0.5f;
		}
	}
	
	private void drawSimpleLines(GL gl) {
		gl.glBegin(GL.GL_LINES);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		float z = 0.0f;
		for(float angle = 0.0f; angle <= Math.PI; angle += (Math.PI / 20.0f)) {
			// Top half of the circle
			float x = 50.0f * ((float) Math.sin(angle));
			float y = 50.0f * ((float) Math.cos(angle));
			gl.glVertex3f(x, y, z); // First endpoint of line
		
			// Bottom half of the circle
			x = 50.0f * ((float) Math.sin(angle + Math.PI));
			y = 50.0f * ((float) Math.cos(angle + Math.PI));
			gl.glVertex3f(x, y, z); // Second endpoint of line
		}
		gl.glEnd();
	}

	@Override public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // black bg
//		gl.glShadeModel(GL.GL_SMOOTH);
//		gl.glDepthFunc(GL.GL_LEQUAL);
//		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	@Override public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		SomeGL.setupCommonViewingVolume(drawable.getGL(), width, height);
	}

	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}
}
