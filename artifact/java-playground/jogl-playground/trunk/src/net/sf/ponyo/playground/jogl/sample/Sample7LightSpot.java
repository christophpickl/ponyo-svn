package net.sf.ponyo.playground.jogl.sample;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import net.sf.ponyo.playground.jogl.sample.SampleInputs.BooleanInput;
import net.sf.ponyo.playground.jogl.sample.SampleInputs.RangeInput;

import com.sun.opengl.util.GLUT;

public class Sample7LightSpot extends AbstractSample {

	private float lightPosf = 10.0f;
	private FloatBuffer lightPos = FloatBuffer.wrap(new float[] { this.lightPosf, this.lightPosf - 2.0f, this.lightPosf, 1.0f});
//	private FloatBuffer spotDir = FloatBuffer.wrap(new float[] {0.3f, 0.3f, 0.3f, 1.0f});
	private FloatBuffer ambientLight = FloatBuffer.wrap(new float[] {0.3f, 0.3f, 0.3f, 1.0f});
	private FloatBuffer specularLight = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	private FloatBuffer globalSpecularReflectance = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});

	private final BooleanInput inpSmooth = new BooleanInput("Smooth", true); // set flat or smooth shading
	private final RangeInput inpSphereParts = new RangeInput("SphereParts", 3.0f, 300.0f, 80.0f);
	private final RangeInput inpXRotate = new RangeInput("X-Rot", -180.0f, 90.0f, 20.0f);
	private final RangeInput inpYRotate = new RangeInput("Y-Rot", -180.0f, 180.0f, 0.0f);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(
		this.inpSmooth, this.inpSphereParts, this.inpXRotate, this.inpYRotate
	));
	
	public Sample7LightSpot() {
//		this.perspectiveFar = 500.0f;
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = new GLUT();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		if(this.inpSmooth.getValue().booleanValue()) gl.glShadeModel(GL.GL_SMOOTH);
		else gl.glShadeModel(GL.GL_FLAT);
		
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, -3.0f, -60.0f);
			this.drawLight(gl, glut);
			this.drawSphere(gl, glut);
			this.drawFloor(gl);
		gl.glPopMatrix();
	}
	
	private void drawFloor(GL gl) {
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, -8.0f, -30.0f);
//			float scaleFloor = 100.0f;
			gl.glScalef(150.0f, 0.0f, 80.0f);
			float yPos = -0.25f;
			gl.glBegin(GL.GL_QUADS);
				gl.glColor3f(0.6f, 0.6f, 0.6f);
				gl.glVertex3f(-1.0f, yPos,  1.0f);
				gl.glVertex3f( 1.0f, yPos,  1.0f);
				// we do manual shading to a darker brown in the background to give the illusion of depth
				gl.glColor3f(0.1f, 0.1f, 0.1f);
				gl.glVertex3f( 1.0f, yPos, -1.0f);
				gl.glVertex3f(-1.0f, yPos, -1.0f);
			gl.glEnd();
		gl.glPopMatrix();
	}
	private void drawSphere(GL gl, GLUT glut) {
		// Set material color and draw a sphere in the middle glColor3ub(0, 0, 255);
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		
		final int sphereParts = Math.round(this.inpSphereParts.getValue().floatValue());
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			glut.glutSolidSphere(7.0f, sphereParts, sphereParts);
		gl.glPopMatrix();
	}
	
	private void drawLight(GL gl, GLUT glut) {
		gl.glPushMatrix(); // Save the coordinate transformation
			gl.glRotatef(this.inpXRotate.getValue().floatValue(), 1.0f, 0.0f, 0.0f);
			gl.glRotatef(this.inpYRotate.getValue().floatValue(), 0.0f, 1.0f, 0.0f);
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, this.lightPos);
//			gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, this.spotDir);
			
			// Draw a red cone to enclose the light source
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			// Translate origin to move the cone out to where the light is positioned.
			gl.glTranslatef(this.lightPos.get(0), this.lightPos.get(1), this.lightPos.get(2));
			glut.glutSolidCone(2.5f, 6.0f, 15, 15);
			
			// Draw a smaller displaced sphere to denote the light bulb
			gl.glPushAttrib(GL.GL_LIGHTING_BIT); // Save the lighting state variables
				gl.glDisable(GL.GL_LIGHTING); // Turn off lighting
				gl.glColor3f(1.0f, 1.0f, 0.0f); // and specify a bright yellow sphere
				glut.glutSolidSphere(2.0f, 15, 15);
			gl.glPopAttrib(); // Restore lighting state variables
		gl.glPopMatrix(); // Restore coordinate transformations
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		gl.glEnable(GL.GL_DEPTH_TEST); // Hidden surface removal
//		gl.glFrontFace(GL.GL_CCW); // Counterclockwise polygons face out
//		gl.glEnable(GL.GL_CULL_FACE); // Do not try to display the back sides
		gl.glEnable(GL.GL_LIGHTING); // Enable lighting
		
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, this.ambientLight); // Supply a slight ambient light so the objects can be seen
		
		// Set up and enable light 0; The light is composed of just diffuse and specular components
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, this.ambientLight);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, this.specularLight);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, this.lightPos);
		
		// Specific spot effects
		gl.glLightf(GL.GL_LIGHT0, GL.GL_SPOT_CUTOFF, 60.0f); // Cut-off angle is 60 degrees
		
		gl.glEnable(GL.GL_LIGHT0); // Enable this light in particular
		gl.glEnable(GL.GL_COLOR_MATERIAL); // Enable color tracking
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE); // Set material properties to follow glColor values
		// All materials hereafter have full specular reflectivity with a high shine
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, this.globalSpecularReflectance);
		gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f ); // Black background
	}

	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}
}
