package net.sf.ponyo.console.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.sf.ponyo.console.view.GLPanel;
import net.sf.ponyo.console.view.JointsDialogManager;
import net.sf.ponyo.jponyo.common.gui.AbstractMainView;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;

public class ConsoleWindow
	extends AbstractMainView<ConsoleWindowListener, Model>
		implements MotionStreamListener, ContinuousUserListener {

	private static final long serialVersionUID = -7367551475234626321L;
	
	private final GLPanel adminPanel = new GLPanel();
	final JointsDialogManager jointsMgr = new JointsDialogManager(this);
	
	public ConsoleWindow(final Model model) {
		super(model, "Jogl Playground");
	}

	@Override
	protected Component initComponent(Model provider) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.adminPanel, BorderLayout.CENTER);
		panel.add(this.initCommandPanel(), BorderLayout.SOUTH);
		return panel;
	}
	
	private Component initCommandPanel() {
		JButton btnToggleSkeletonDialog = new JButton("Toggle Joints Dialog");
		btnToggleSkeletonDialog.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent actionevent) {
			ConsoleWindow.this.jointsMgr.toggleDialog();
		}});
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() { @SuppressWarnings("synthetic-access")
		public void actionPerformed(ActionEvent e) {
			dispatchQuit();
		}});
		this.getRootPane().setDefaultButton(btnQuit);
		
		JPanel cmdPanel = new JPanel();
		cmdPanel.add(btnToggleSkeletonDialog);
		cmdPanel.add(btnQuit);
		return cmdPanel;
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible) {
			this.adminPanel.start();
		} else {
			this.adminPanel.stop();
		}
		super.setVisible(visible);
		this.jointsMgr.setVisible(visible);
	}
	
//	public void setUser(User user) {
//		this.adminPanel.onCurrentUserChanged(user);
//	}

	public void onCurrentUserChanged(User user) {
		this.adminPanel.onCurrentUserChanged(user);
	}
	
	public void onMotion(MotionData data) {
		this.jointsMgr.onMotion(data);
	}

	public void destroy() {
		this.adminPanel.stop();
		
		this.setVisible(false);
		
		this.jointsMgr.dispose();
		this.dispose();
	}

}
