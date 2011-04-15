package net.sf.ponyo.jponyo.adminconsole.gl;

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
	
//	public static void translate(GL gl, GlobalData data, Skel skel) {
//		final int skelId = skel.getId();
//		final float x = data.xByJoint[skelId] / 100.0f;
//		final float y = data.yByJoint[skelId] / 100.0f;
//		final float z = data.zByJoint[skelId] / 100.0f - 30;
////		System.out.println("translated x/y/z: "+x+"/"+y+"/"+z);
//		gl.glTranslatef(x, y, z);
//	}
}
