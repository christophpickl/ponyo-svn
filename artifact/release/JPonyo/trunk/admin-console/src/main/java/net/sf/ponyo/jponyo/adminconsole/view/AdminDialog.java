package net.sf.ponyo.jponyo.adminconsole.view;

import javax.swing.JDialog;

import net.sf.ponyo.jponyo.user.User;

public class AdminDialog extends JDialog {

	private static final long serialVersionUID = 1716630211270484837L;
	
	private final AdminPanel panel;
	
	public AdminDialog() {

		this.setTitle("Admin Console");
		this.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
		
		this.panel = new AdminPanel();
		this.getContentPane().add(this.panel);
	    this.setSize(600, 430);
	}
	
	public void setUser(User user) {
		this.panel.setUser(user);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			this.panel.start();
		} else {
			this.panel.stop();
		}
		super.setVisible(visible);
	}
	
}
