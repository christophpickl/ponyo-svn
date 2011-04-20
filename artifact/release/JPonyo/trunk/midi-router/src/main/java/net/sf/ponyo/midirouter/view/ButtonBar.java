package net.sf.ponyo.midirouter.view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonBar extends JPanel {

	private static final long serialVersionUID = -4832983778195118152L;
	
	public ButtonBar() {
		JButton btnStartStop = new JButton("Start");
		btnStartStop.setToolTipText("Just start that damn kreiwl!");
		
		JButton btnReload = new JButton("Reload");
		btnReload.setToolTipText("On-the-fly reload mappings");
		
		this.setLayout(new FlowLayout());
		this.add(btnStartStop);
		this.add(btnReload);
	}
	
}
