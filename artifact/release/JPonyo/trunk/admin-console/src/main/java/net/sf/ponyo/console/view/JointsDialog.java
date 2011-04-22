package net.sf.ponyo.console.view;

import javax.swing.JDialog;

import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;

public class JointsDialog extends JDialog implements MotionStreamListener {
	
	private static final long serialVersionUID = -7567626819040272422L;
	
	private final JointsPanel panel;
	
	public JointsDialog() {
		this.panel = new JointsPanel();
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
