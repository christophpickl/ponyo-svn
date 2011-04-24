package net.sf.ponyo.playground.jogl.sample;

import java.util.Collection;

import javax.media.opengl.GLEventListener;

public interface Sample extends GLEventListener {

	Collection<SampleInput> getInputs();
	
}
