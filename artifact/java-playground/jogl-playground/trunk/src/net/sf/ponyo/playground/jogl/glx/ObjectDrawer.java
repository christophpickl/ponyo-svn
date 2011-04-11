package net.sf.ponyo.playground.jogl.glx;

import java.awt.Color;

import javax.media.opengl.GL;

public final class ObjectDrawer {
	private ObjectDrawer() {
		// static only
	}
	
	private static void setColorOrDefault(GL gl, Color c, float r, float g, float b) {
		if(c == null) gl.glColor3f(r, g, b);
		else GLUtil.setColor(gl, c);
	}
	
	public static void drawPyramid(GL gl) {
		drawPyramid(gl, null);
	}
	
	public static void drawPyramid(GL gl, Color c){
		gl.glBegin(GL.GL_TRIANGLES);
		
		setColorOrDefault(gl, c, 1.0f,0.0f,0.0f);
		gl.glVertex3f( 0.0f, 1.0f, 0.0f); // top
		setColorOrDefault(gl, c, 0.0f,1.0f,0.0f);			
		gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
		setColorOrDefault(gl, c, 0.0f,0.0f,1.0f);			
		gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
		setColorOrDefault(gl, c, 1.0f,0.0f,0.0f);			
		gl.glVertex3f( 0.0f, 1.0f, 0.0f); // top
		
		setColorOrDefault(gl, c, 0.0f,0.0f,1.0f);			
		gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
		setColorOrDefault(gl, c, 0.0f,1.0f,0.0f);			
		gl.glVertex3f( 1.0f,-1.0f, -1.0f);			
		setColorOrDefault(gl, c, 1.0f,0.0f,0.0f);			
		gl.glVertex3f( 0.0f, 1.0f, 0.0f); // top
		setColorOrDefault(gl, c, 0.0f,1.0f,0.0f);			
		gl.glVertex3f( 1.0f,-1.0f, -1.0f);			
		setColorOrDefault(gl, c, 0.0f,0.0f,1.0f);			
		gl.glVertex3f(-1.0f,-1.0f, -1.0f);			
		setColorOrDefault(gl, c, 1.0f,0.0f,0.0f);			
		gl.glVertex3f( 0.0f, 1.0f, 0.0f); // top
		setColorOrDefault(gl, c, 0.0f,0.0f,1.0f);			
		gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
		setColorOrDefault(gl, c, 0.0f,1.0f,0.0f);			
		gl.glVertex3f(-1.0f,-1.0f, 1.0f);	
		
		gl.glEnd();	
	}
	
	public static void drawCube(GL gl) {
		drawCube(gl, null);
	}
	
	public static void drawCube(GL gl, Color c) {
		gl.glBegin(GL.GL_QUADS);
		
		setColorOrDefault(gl, c, 0.0f, 1.0f, 0.0f);
		gl.glVertex3f( 1.0f, 1.0f,-1.0f);			
		gl.glVertex3f(-1.0f, 1.0f,-1.0f);			
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);			
		gl.glVertex3f( 1.0f, 1.0f, 1.0f);
		
		setColorOrDefault(gl, c, 1.0f,0.5f,0.0f);
		gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
		gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
		gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
		gl.glVertex3f( 1.0f,-1.0f,-1.0f);	
		
		setColorOrDefault(gl, c, 1.0f,0.0f,0.0f);
		gl.glVertex3f( 1.0f, 1.0f, 1.0f);			
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);			
		gl.glVertex3f(-1.0f,-1.0f, 1.0f);			
		gl.glVertex3f( 1.0f,-1.0f, 1.0f);
		
		setColorOrDefault(gl, c, 1.0f,1.0f,0.0f);
		gl.glVertex3f( 1.0f,-1.0f,-1.0f);			
		gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
		gl.glVertex3f(-1.0f, 1.0f,-1.0f);			
		gl.glVertex3f( 1.0f, 1.0f,-1.0f);

		setColorOrDefault(gl, c, 0.0f,0.0f,1.0f);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);			
		gl.glVertex3f(-1.0f, 1.0f,-1.0f);			
		gl.glVertex3f(-1.0f,-1.0f,-1.0f);			
		gl.glVertex3f(-1.0f,-1.0f, 1.0f);

		setColorOrDefault(gl, c, 1.0f,0.0f,1.0f);
		gl.glVertex3f( 1.0f, 1.0f,-1.0f);			
		gl.glVertex3f( 1.0f, 1.0f, 1.0f);			
		gl.glVertex3f( 1.0f,-1.0f, 1.0f);			
		gl.glVertex3f( 1.0f,-1.0f,-1.0f);
		
		gl.glEnd();	
	}
}
