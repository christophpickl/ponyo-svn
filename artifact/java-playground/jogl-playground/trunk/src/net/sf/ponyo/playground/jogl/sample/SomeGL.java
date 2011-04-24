package net.sf.ponyo.playground.jogl.sample;

import javax.media.opengl.GL;

public class SomeGL {

	private static final float RANGE = 100.0f;
	public static void setupCommonViewingVolume(GL gl, int width, int maybeZeroHeight) {
		int height = maybeZeroHeight == 0 ? 1 : maybeZeroHeight;
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		if (width <= height)
			gl.glOrtho(-RANGE, RANGE, -RANGE * height / width, RANGE * height / width, -RANGE, RANGE);
		else
			gl.glOrtho(-RANGE * width / height, RANGE * width / height, -RANGE, RANGE, -RANGE, RANGE);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	// Store supported point size range/increments
//	FloatBuffer sizes = FloatBuffer.allocate(2);
//	FloatBuffer step = FloatBuffer.allocate(1);
	// Get supported point size range and step size
//	gl.glGetFloatv(GL.GL_POINT_SIZE_RANGE, sizes);
//	gl.glGetFloatv(GL.GL_POINT_SIZE_GRANULARITY, step);
//	System.out.println("sizes/step: " + sizes.get(0) + "-" + sizes.get(1) + " / " + step.get(0));
	// => sizes/step: 1.0-63.375 / 0.125
	
}
