package net.sf.ponyo.playground.jogl.glx;

import java.awt.Color;

import javax.media.opengl.GL;

public final class GLUtil {
	
	private GLUtil() { /* utility class */ }
	
	public static void setColor(GL gl, Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
//		System.out.println("r / g / b = " + r + " / " + g + " / " + b);
		float rf = r / 255.0f;
		float gf = g / 255.0f;
		float bf = b / 255.0f;
//		System.out.println("FLOAT r / g / b = " + rf + " / " + gf + " / " + bf);
		gl.glColor3f(rf, gf, bf);
	}
}
