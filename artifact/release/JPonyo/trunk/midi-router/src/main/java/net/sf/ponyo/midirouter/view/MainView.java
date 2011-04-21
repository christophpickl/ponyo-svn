package net.sf.ponyo.midirouter.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import net.sf.ponyo.jponyo.common.async.Async;

public interface MainView extends Async<MainViewListener> {

	void dispose();

	void setVisible(boolean b);

	void display(Dimension dimension);
	
	JFrame asJFrame();

}