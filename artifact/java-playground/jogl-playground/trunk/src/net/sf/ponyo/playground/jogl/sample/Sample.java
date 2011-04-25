package net.sf.ponyo.playground.jogl.sample;

import java.util.Collection;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;

public interface Sample extends GLEventListener {

	Collection<SampleInput> getInputs();

	void setKeyLeftPressed(boolean pressed);
	void setKeyRightPressed(boolean pressed);
	void setKeyUpPressed(boolean pressed);
	void setKeyDownPressed(boolean pressed);

	void initGL(GLCapabilities capabilities);
	
}
