package net.sf.ponyo.jponyo.adminconsole.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;

public class AdminDialog extends JDialog implements MotionStreamListener, ContinuousUserListener {

	private static final long serialVersionUID = 1716630211270484837L;
	
	private final AdminPanel adminPanel = new AdminPanel();

	private SkeletonDataDialog skeletonDialog;

//	this.skeletonDialog = new SkeletonDataDialog();
	public AdminDialog() {
		this.setTitle("Admin Console");

		this.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
		
		JPanel commandPanel = new JPanel();
		JButton btnToggleSkeletonDialog = new JButton("Toggle Skeleton Dialog");
		btnToggleSkeletonDialog.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent actionevent) {
			onToggleSkeletonDialog();
		}});
		commandPanel.add(btnToggleSkeletonDialog);
		
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(this.adminPanel, BorderLayout.CENTER);
		rootPanel.add(commandPanel, BorderLayout.SOUTH);
		this.getContentPane().add(rootPanel);
		
	    this.setSize(600, 430);
	}
	
	void onToggleSkeletonDialog() {
		if(this.skeletonDialog != null) {
			this.skeletonDialog.setVisible(!this.skeletonDialog.isVisible());
		} else {
			this.skeletonDialog = new SkeletonDataDialog();
			Dimension windowSize = this.getSize();
			Point windowLocation = this.getLocation();
			this.skeletonDialog.setLocation(windowLocation.x + windowSize.width + 4, windowLocation.y);
			this.skeletonDialog.setVisible(true);
		}
	}

	public void onCurrentUserChanged(User user) {
		this.adminPanel.onCurrentUserChanged(user);
	}

	public void onMotion(MotionData data) {
		if(this.skeletonDialog != null) {
			this.skeletonDialog.onMotion(data);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			this.adminPanel.start();
		} else {
			this.adminPanel.stop();
		}
		
		if(this.skeletonDialog != null) {
			this.skeletonDialog.setVisible(visible);
		}
		
		super.setVisible(visible);
	}
	
	@Override
	public void dispose() {
		if(this.skeletonDialog != null) {
			this.skeletonDialog.dispose();
		}
	}
}
