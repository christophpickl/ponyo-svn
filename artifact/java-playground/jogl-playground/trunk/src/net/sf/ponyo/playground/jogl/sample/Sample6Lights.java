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

	private final BooleanInput inpLightEnabled = new BooleanInput("Light", true);
	private final Collection<SampleInput> inputs = new ArrayList<SampleInput>(Arrays.asList(
			this.inpLightEnabled
		));
	
	private FloatBuffer teapotColor = FloatBuffer.wrap(new float[] {1.0f, 0.0f, 0.0f, 1.0f});
	private FloatBuffer teapotMaterialLight = FloatBuffer.wrap(new float[] {0.0f, 1.0f, 0.0f, 1.0f});
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLUT glut = new GLUT();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		if(this.inpLightEnabled.getValue().booleanValue())
			gl.glEnable(GL.GL_LIGHTING);
		else
			gl.glDisable(GL.GL_LIGHTING);
		
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -200.0f);
		
		gl.glColor3fv(this.teapotColor);
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, this.teapotMaterialLight);
		
		glut.glutSolidTeapot(20.0f);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//		gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		FloatBuffer ambientLight = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
		// Set light model to use ambient light specified by ambientLight[]
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, ambientLight);
	}

	@Override public Collection<SampleInput> getInputs() {
		return this.inputs;
	}
}
