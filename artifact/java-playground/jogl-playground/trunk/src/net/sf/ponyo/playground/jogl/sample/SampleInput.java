package net.sf.ponyo.playground.jogl.sample;

import java.awt.Component;

public interface SampleInput {
	String getLabel();
	Object getValue();
	Component asComponent();
}
