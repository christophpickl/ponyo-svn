package net.sf.ponyo.jponyo.adminconsole.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.adminconsole.view.GLPanel;
import net.sf.ponyo.jponyo.adminconsole.view.JointsDialogManager;
import net.sf.ponyo.jponyo.common.gui.AbstractMainView;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.User;

public class ConsoleWindow extends AbstractMainView<ConsoleWindowListener, Model> implements MotionStreamListener {

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

		JPanel cmdPanel = new JPanel();
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			dispatchQuit();
		}});
		cmdPanel.add(btnQuit);
		
		panel.add(cmdPanel, BorderLayout.SOUTH);

		this.getRootPane().setDefaultButton(btnQuit);
		
		return panel;
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
	
	public void setUser(User user) {
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
