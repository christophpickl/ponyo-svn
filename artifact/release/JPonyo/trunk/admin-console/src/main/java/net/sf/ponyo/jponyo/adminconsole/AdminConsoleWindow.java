package net.sf.ponyo.jponyo.adminconsole;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.adminconsole.view.AdminPanel;
import net.sf.ponyo.jponyo.adminconsole.view.AdminPanelListener;
import net.sf.ponyo.jponyo.core.GlobalSpace;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.User;

public class AdminConsoleWindow extends JFrame implements MotionStreamListener {

	private static final long serialVersionUID = -7367551475234626321L;
	
//	private final SkeletonNumberDialog skeletonDialog = new SkeletonNumberDialog();
	private final AdminPanel adminPanel;
	
	public AdminConsoleWindow(final AdminPanelListener listener) {
		super("Jogl Playground");
		
		JPanel panel = new JPanel(new BorderLayout());
		this.adminPanel = new AdminPanel();
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
//	@Override public void setVisible(boolean visible) {
//		super.setVisible(visible);
//		Point p = this.getLocation();
//		this.skeletonDialog.setLocation(p.x + this.getWidth() + 5/*gap*/, p.y);
//		this.skeletonDialog.setVisible(visible);
//	}
	
	public void display() {
		this.adminPanel.start();
		this.setVisible(true);
	}
	public void setUser(User user) {
		this.adminPanel.onCurrentUserChanged(user);
	}

	public void destroy() {
		this.adminPanel.stop();
		this.setVisible(false);
		this.dispose();
	}
	public void onMotion(MotionData data) {
		// TODO 
//		this.skeletonDialog.update(this.data);
	}

}
