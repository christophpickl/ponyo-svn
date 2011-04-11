package jponyo.gui;

import javax.swing.JDialog;

import jponyo.GlobalData;

public class SkeletonNumberDialog extends JDialog {
	
	private static final long serialVersionUID = -7567626819040272422L;
	
	private final SkeletonNumberPanel panel = new SkeletonNumberPanel();
	
	public SkeletonNumberDialog() {
		this.setTitle("Raw Joint Data");
		this.getContentPane().add(this.panel);
		this.getRootPane().putClientProperty("Window.style", "small");
		this.pack();
		this.setResizable(false);
	}
	
	public void update(GlobalData data) {
		this.panel.update(data);
	}
	
}
