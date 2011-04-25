package net.sf.ponyo.playground.jogl.sample;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;

import net.sf.ponyo.playground.jogl.glfacade.GX;
import net.sf.ponyo.playground.jogl.sample.SampleInputs.BooleanInput;

import com.sun.opengl.util.GLUT;

public class Sample8Blending extends AbstractSample {

	private FloatBuffer lightPosition       = FloatBuffer.wrap(new float[] { 1.0f, 5.0f, 3.0f });
	private FloatBuffer fLightPosMirror = FloatBuffer.wrap(new float[] { 1.0f, -5.0f, 3.0f });
	
	private FloatBuffer ambientLight = FloatBuffer.wrap(new float[] {0.3f, 0.3f, 0.3f, 1.0f});
	private FloatBuffer specularLight = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	private FloatBuffer globalSpecularReflectance = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	
	// Another important note about multisampling is that when it is enabled,
	// the point, line, and polygon smoothing features are ignored if enabled! 
	private final boolean isMultisampleEnabled = true;
	private final boolean isAntialiasingEnabled = true;
	
	private final int sphereParts = 70;
	private float theOneSphereRotation = 0.0f;
	private float floorAlpha = 0.8f;
	
//	private FloatBuffer fogLight = FloatBuffer.wrap(new float[] { 0.6f, 0.6f, 0.6f, 1.0f });
	private FloatBuffer fogLight = FloatBuffer.wrap(new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
	
	private final BooleanInput inpLines = new BooleanInput("Lines", false);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(
			this.inpLines
		));
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = new GLUT();
		GX.clearColorAndDepthBuffer(gl);
		if(this.inpLines.getValue().booleanValue()) GX.PolygonMode.LINE.frontAndBack(gl);
		else GX.PolygonMode.FILL.frontAndBack(gl);
		
		gl.glPushMatrix();
			GX.translateZ(gl, -50.0f); // put everything further away
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, this.fLightPosMirror); // Move light under floor to light the "reflected" world
			
			gl.glPushMatrix(); // draw upside down world
				GX.FrontFace.CLOCKWISE.with(gl); // geometry is mirrored, swap orientation
				gl.glScalef(1.0f, -1.0f, 1.0f);
				GX.translateY(gl, 18.0f); // HACK: adjust reflection by moving it down a bit
				this.drawWorld(gl, glut);
				GX.FrontFace.COUNTER_CLOCKWISE.with(gl);
			gl.glPopMatrix();
			
			// Draw the ground transparently over the reflection
			GX.Turn.LIGHTING.off(gl);
			GX.Turn.BLEND.on(gl);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			if(this.inpLines.getValue().booleanValue()) GX.PolygonMode.FILL.frontAndBack(gl);
			this.drawGround(gl);
			if(this.inpLines.getValue().booleanValue()) GX.PolygonMode.LINE.frontAndBack(gl);
			GX.Turn.BLEND.off(gl);
			GX.Turn.LIGHTING.on(gl);
			
//			// Restore correct lighting and draw the world correctly
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, this.lightPosition);
			drawWorld(gl, glut);
		gl.glPopMatrix();
		
//		FIXME haeh?? drawable.swapBuffers();
	}
	
	private void drawWorld(GL gl, GLUT glut) {
		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 5.0f, -50.0f);
		GX.drawSolidSphere(glut, 11.0f, this.sphereParts);
		
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		GX.rotateY(gl, this.theOneSphereRotation);
		GX.translateX(gl, 40.0f);
		GX.drawSolidSphere(glut, 8.0f, this.sphereParts);
		
		GX.rotateY(gl, this.theOneSphereRotation); // == gl.glRotatef(this.rotation, 0.0f, 1.0f, 0.0f);
		GX.translateX(gl, 20.0f);
		gl.glColor3f(0.7f, 0.7f, 0.7f);
		GX.drawSolidSphere(glut, 2.0f, this.sphereParts);
		
		this.theOneSphereRotation += 0.5f;
		if(this.theOneSphereRotation >= 360.0f) this.theOneSphereRotation = 0;
	}
	
	private void drawGround(GL gl) {
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, -10.0f, -30.0f);
			gl.glScalef(200.0f, 0.0f, 80.0f);
			gl.glBegin(GL.GL_QUADS);
				gl.glColor4f(0.6f, 0.6f, 0.6f, this.floorAlpha);
				gl.glVertex3f(-1.0f, 0.0f,  1.0f);
				gl.glVertex3f( 1.0f, 0.0f,  1.0f);
				gl.glColor4f(0.1f, 0.1f, 0.1f, this.floorAlpha);
				gl.glVertex3f( 1.0f, 0.0f, -1.0f);
				gl.glVertex3f(-1.0f, 0.0f, -1.0f);
			gl.glEnd();
		gl.glPopMatrix();
	}
	
	@Override public void initGL(GLCapabilities capabilities) {
		super.initGL(capabilities);
		
		if(this.isMultisampleEnabled) {
			capabilities.setSampleBuffers(true);
			capabilities.setNumSamples(2);
		}
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		GX.Turn.DEPTH_TEST.on(gl);
		GX.ShadeModel.SMOOTH.with(gl);
		
		GX.Turn.BLEND.on(gl);
//		gl.glBlendEquation(GL.GL_FUNC_ADD); ... default
		if(this.isAntialiasingEnabled) {
			GX.Turn.SMOOTH_POINT.on(gl);
			GX.Hint.POINT_SMOOTH.nicest(gl);
			
			GX.Turn.SMOOTH_LINE.on(gl);
			GX.Hint.LINE_SMOOTH.nicest(gl);
			
			GX.Turn.SMOOTH_POLYGON.on(gl);
			GX.Hint.POLYGON_SMOOTH.nicest(gl);
		}
		
		if(this.isMultisampleEnabled) {
			GX.Turn.MULTISAMPLE.on(gl);
		}
		
		this.initLight(gl);
		
//		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f );
		GX.clearColor(gl, this.fogLight); // background color is same as fog color
		
		GX.enableFog(gl, this.fogLight, 50.0f, 160.0f);
		GX.Hint.FOG.nicest(gl);
	}
	
	private void initLight(GL gl) {
		GX.Turn.LIGHTING.on(gl);

		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, this.ambientLight); // Supply a slight ambient light so the objects can be seen
		
		// Set up and enable light 0; The light is composed of just diffuse and specular components
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, this.ambientLight);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, this.specularLight);
		gl.glLightf(GL.GL_LIGHT0, GL.GL_SPOT_CUTOFF, 60.0f); // Specific spot effect: Cut-off angle is 60 degrees
		
		gl.glEnable(GL.GL_LIGHT0); // Enable this light in particular
		gl.glEnable(GL.GL_COLOR_MATERIAL); // Enable color tracking
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE); // Set material properties to follow glColor values
		// All materials hereafter have full specular reflectivity with a high shine
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, this.globalSpecularReflectance);
		gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);
	}
	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}
}
