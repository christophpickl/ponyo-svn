package net.sf.ponyo.playground.jogl.glfacade;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class GX {
	
	public static void clearColorAndDepthBuffer(GL gl) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void clearColor(GL gl, float r, float g, float b) {
		gl.glClearColor(r, g, b, 1.0f);
	}
	public static void clearColor(GL gl, FloatBuffer floats) {
		if(floats.capacity() == 3) {
			gl.glClearColor(floats.get(0), floats.get(1), floats.get(2), 1.0f);
		} else if(floats.capacity() == 4) {
			gl.glClearColor(floats.get(0), floats.get(1), floats.get(2), floats.get(3));
		} else {
			throw new RuntimeException("Invalid floats size: " + floats.capacity());
		}
	}
	public static void rotateX(GL gl, float angle) {
		gl.glRotatef(angle, 1.0f, 0.0f, 0.0f);
	}
	public static void rotateY(GL gl, float angle) {
		gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
	}
	public static void rotateZ(GL gl, float angle) {
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
	}
	public static void translateX(GL gl, float value) {
		gl.glTranslatef(value, 0.0f, 0.0f);
	}
	public static void translateY(GL gl, float value) {
		gl.glTranslatef(0.0f, value, 0.0f);
	}
	public static void translateZ(GL gl, float value) {
		gl.glTranslatef(0.0f, 0.0f, value);
	}
	
	public static void colorGreyish(GL gl, float value) {
		gl.glColor3f(value, value, value);
	}
	public static void drawSolidSphere(GLUT glut, float size, int sphereParts) {
		glut.glutSolidSphere(size, sphereParts, sphereParts);
	}

	public static enum ShadeModel {
		SMOOTH(GL.GL_SMOOTH),
		FLAT(GL.GL_FLAT);
		private final int glCode;
		private ShadeModel(int glCode) {
			this.glCode = glCode;
		}
		public void with(GL gl) {
			gl.glShadeModel(this.glCode);
		}
	}
	public static enum PolygonMode {
		FILL(GL.GL_FILL),
		LINE(GL.GL_LINE);
		private final int glCode;
		private PolygonMode(int glCode) {
			this.glCode = glCode;
		}
		public void frontAndBack(GL gl) {
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, this.glCode);
		}
		public void front(GL gl) {
			gl.glPolygonMode(GL.GL_FRONT, this.glCode);
		}
		public void back(GL gl) {
			gl.glPolygonMode(GL.GL_BACK, this.glCode);
		}
	}
	
	/** Aka glEnable */
	public static enum Turn {
		DEPTH_TEST(GL.GL_DEPTH_TEST),
		BLEND(GL.GL_BLEND),
		LIGHTING(GL.GL_LIGHTING),
		MULTISAMPLE(GL.GL_MULTISAMPLE),
		SMOOTH_POINT(GL.GL_POINT_SMOOTH),
		SMOOTH_LINE(GL.GL_LINE_SMOOTH),
		SMOOTH_POLYGON(GL.GL_POLYGON_SMOOTH),
		;
		
		private final int glCode;
		private Turn(int glCode) { this.glCode = glCode; }
		public void on(GL gl) { gl.glEnable(this.glCode); }
		public void off(GL gl) { gl.glDisable(this.glCode); }
	}

	public static enum FrontFace {
		CLOCKWISE(GL.GL_CW),
		COUNTER_CLOCKWISE(GL.GL_CCW);
		
		private final int glCode;
		private FrontFace(int glCode) { this.glCode = glCode; }
		public void with(GL gl) { gl.glFrontFace(this.glCode); }
	}
	
	public static enum Hint {
		FOG(GL.GL_FOG_HINT),
		POINT_SMOOTH(GL.GL_POINT_SMOOTH_HINT),
		LINE_SMOOTH(GL.GL_LINE_SMOOTH_HINT),
		POLYGON_SMOOTH(GL.GL_POLYGON_SMOOTH_HINT)
		;
		
		private final int glCode;
		private Hint(int glCode) { this.glCode = glCode; }
		public void nicest(GL gl) { gl.glHint(this.glCode, GL.GL_NICEST); }
		public void fastest(GL gl) { gl.glHint(this.glCode, GL.GL_FASTEST); }
	}
	
	public static void enableFog(GL gl, FloatBuffer fogLight, float fogStart, float fogEnd) {
		gl.glEnable(GL.GL_FOG); // Turn Fog on
//		if(fogEquation != FogEquation.LINEAR) {
//			gl.glFogf(GL.GL_FOG_DENSITY, 0.5f);
//		}
		gl.glFogfv(GL.GL_FOG_COLOR, fogLight); // Set fog color to match background
		gl.glFogf(GL.GL_FOG_START, fogStart); // How far away does the fog start
		gl.glFogf(GL.GL_FOG_END, fogEnd); // How far away does the fog stop
		gl.glFogi(GL.GL_FOG_MODE, GL.GL_LINEAR);
//		fogEquation.with(gl); // Which fog equation to use
	}
//	public static enum FogEquation {
//		LINEAR(GL.GL_LINEAR),
//		EXP(GL.GL_EXP),
//		EXP2(GL.GL_EXP2)
//		;
//		private final int glCode;
//		private FogEquation(int glCode) { this.glCode = glCode; }
//		public void with(GL gl) { gl.glFogi(GL.GL_FOG_MODE, this.glCode); }
//	}
}
