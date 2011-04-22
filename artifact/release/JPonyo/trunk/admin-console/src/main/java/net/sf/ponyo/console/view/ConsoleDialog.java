package net.sf.ponyo.console.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;

public class ConsoleDialog
	extends JDialog
		implements MotionStreamListener, ContinuousUserListener {

	private static final long serialVersionUID = 1716630211270484837L;
	
	private final GLPanel adminPanel = new GLPanel();

	final JointsDialogManager jointsMgr = new JointsDialogManager(this);

	public ConsoleDialog() {
		this.setTitle("Admin Console");

		this.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
		
		JPanel commandPanel = new JPanel();
		JButton btnToggleSkeletonDialog = new JButton("Toggle Joints Dialog");
		btnToggleSkeletonDialog.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent actionevent) {
			ConsoleDialog.this.jointsMgr.toggleDialog();
		}});
		commandPanel.add(btnToggleSkeletonDialog);
		
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(this.adminPanel, BorderLayout.CENTER);
		rootPanel.add(commandPanel, BorderLayout.SOUTH);
		this.getContentPane().add(rootPanel);
		
	    this.setSize(600, 430);
	}
	

	public void onCurrentUserChanged(User user) {
		this.adminPanel.onCurrentUserChanged(user);
	}

	public void onMotion(MotionData data) {
		this.jointsMgr.onMotion(data);
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			this.adminPanel.start();
		} else {
			this.adminPanel.stop();
		}
		this.jointsMgr.setVisible(visible);
		super.setVisible(visible);
	}
	
	@Override
	public void dispose() {
		this.jointsMgr.dispose();
	}
}
