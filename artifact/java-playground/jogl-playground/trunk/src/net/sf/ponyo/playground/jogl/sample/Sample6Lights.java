package net.sf.ponyo.playground.jogl.sample;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import net.sf.ponyo.playground.jogl.sample.SampleInputs.BooleanInput;

import com.sun.opengl.util.GLUT;

public class Sample6Lights extends AbstractSample {

	private final BooleanInput inpLines = new BooleanInput("Lines", false);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(
			this.inpLines
		));
	
	private FloatBuffer ambientLight = FloatBuffer.wrap(new float[] {0.3f, 0.3f, 0.3f, 1.0f});
	private FloatBuffer diffuseLight = FloatBuffer.wrap(new float[] {0.7f, 0.7f, 0.7f, 1.0f});
	private FloatBuffer specularLight = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	private FloatBuffer allTeapotSpecularReflectance = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	
	private FloatBuffer teapotColor = FloatBuffer.wrap(new float[] {1.0f, 0.0f, 0.0f, 1.0f});
	private FloatBuffer teapotColor2 = FloatBuffer.wrap(new float[] {0.0f, 1.0f, 0.0f, 1.0f});
	private FloatBuffer teapotColor3 = FloatBuffer.wrap(new float[] {0.0f, 0.0f, 1.0f, 1.0f});
//	private FloatBuffer teapotMaterialLight = FloatBuffer.wrap(new float[] {0.0f, 1.0f, 0.0f, 1.0f});
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = new GLUT();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		if(this.inpLines.getValue().booleanValue())
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		else
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		
		// without GL_COLOR_MATERIAL, to set material light properties:
//		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, this.teapotMaterialLight);

		gl.glLoadIdentity();
		gl.glColor3fv(this.teapotColor);
		gl.glTranslatef(0.0f, 0.0f, -50.0f);
		gl.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);
		glut.glutSolidTeapot(20.0f);
		
		gl.glColor3fv(this.teapotColor2);
		gl.glTranslatef(45.0f, 5.0f, -70.0f);
		gl.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
		glut.glutSolidTeapot(20.0f);

		gl.glColor3fv(this.teapotColor3);
		gl.glTranslatef(60.0f, 10.0f, -30.0f);
		gl.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(-20.0f, 0.0f, 0.0f, 1.0f);
		glut.glutSolidTeapot(20.0f);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		gl.glClearDepth(1.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//		gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
		
//		gl.glFrontFace(GL.GL_CW);
//		gl.glEnable(GL.GL_CULL_FACE); // do not calculate inside
			
		
		// Set light model to use ambient light specified by ambientLight[]
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, this.ambientLight);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, this.ambientLight);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, this.diffuseLight);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, this.specularLight);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		
		// All materials hereafter have full specular reflectivity with a high shine
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, this.allTeapotSpecularReflectance);
		gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS,128);
		
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE); // Set material properties to follow glColor values
		gl.glEnable(GL.GL_NORMALIZE); // Rescale normals to unit length
	}

	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}
}
