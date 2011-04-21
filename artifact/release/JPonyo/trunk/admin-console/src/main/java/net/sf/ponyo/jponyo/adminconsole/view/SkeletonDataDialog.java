package net.sf.ponyo.jponyo.adminconsole.view;

import javax.swing.JDialog;

import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;

public class SkeletonDataDialog extends JDialog implements MotionStreamListener {
	
	private static final long serialVersionUID = -7567626819040272422L;
	
	private final SkeletonDataPanel panel;
	
	public SkeletonDataDialog() {
		this.panel = new SkeletonDataPanel();
		this.setTitle(this.getClass().getSimpleName());
		this.getContentPane().add(this.panel);
		this.getRootPane().putClientProperty("Window.style", "small");
		this.pack();
		this.setResizable(false);
	}
	
	public void onMotion(MotionData data) {
		this.panel.update(data);
	}
	
}
