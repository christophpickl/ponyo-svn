package net.sf.ponyo.jponyo.adminconsole.view;

import javax.swing.JDialog;

import net.sf.ponyo.jponyo.core.GlobalSpace;

public class SkeletonNumberDialog extends JDialog {
	
	private static final long serialVersionUID = -7567626819040272422L;
	
	private final SkeletonNumberPanel panel;
	
	public SkeletonNumberDialog(GlobalSpace space) {
		this.panel = new SkeletonNumberPanel(space);
		this.setTitle(this.getClass().getSimpleName());
		this.getContentPane().add(this.panel);
		this.getRootPane().putClientProperty("Window.style", "small");
		this.pack();
		this.setResizable(false);
	}
	
	public void update() {
		this.panel.update();
	}
	
}
