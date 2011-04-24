package net.sf.ponyo.playground.jogl.sample;

import java.util.Collection;

import javax.media.opengl.GLAutoDrawable;

public abstract class AbstractSample implements Sample {
	
	@Override
	public Collection<SampleInput> getInputs() {
		return null;
	}

	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// empty
	}
	
}
