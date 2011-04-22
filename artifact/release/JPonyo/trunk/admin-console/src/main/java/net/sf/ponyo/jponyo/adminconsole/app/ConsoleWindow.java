package net.sf.ponyo.jponyo.adminconsole.app;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.adminconsole.view.GLPanel;
import net.sf.ponyo.jponyo.adminconsole.view.JointsDialog;
import net.sf.ponyo.jponyo.adminconsole.view.JointsDialogManager;
import net.sf.ponyo.jponyo.core.GlobalSpace;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.User;

public class ConsoleWindow extends JFrame implements MotionStreamListener {

	private static final long serialVersionUID = -7367551475234626321L;
	
	private final GLPanel adminPanel = new GLPanel();
	final JointsDialogManager jointsMgr = new JointsDialogManager(this);
	
	public ConsoleWindow(final ConsoleWindowListener listener) {
		super("Jogl Playground");
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.adminPanel, BorderLayout.CENTER);

		JPanel cmdPanel = new JPanel();
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			listener.onQuit();
		}});
		cmdPanel.add(btnQuit);
		
		panel.add(cmdPanel, BorderLayout.SOUTH);
		this.getContentPane().add(panel);
		
		this.getRootPane().setDefaultButton(btnQuit);
	    
	    this.addWindowListener(new WindowAdapter() {
	        @Override public void windowClosing(WindowEvent e) {
	        	System.out.println("windowClosing()");
	        	
	        	// TODO why do we start another thread for that?!
	        	new Thread(new Runnable() {
					public void run() {
	        			listener.onQuit();
	        		}
	        	}).start();
	        }
	    });
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
