package net.sf.ponyo.jponyo.samples.context;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.sf.ponyo.jponyo.DevelopmentConstants;
import net.sf.ponyo.jponyo.PonyoContext;

public class SimpleContextSample {
	
	public static void main(String[] args) {
		new SimpleContextSample().doit();
	}
	
	public void doit() {
		final Window loadingWindow = this.showLoadingWindow();
		
		final PonyoContext ponyoContext = new PonyoContext();
		ponyoContext.startOniRecording(DevelopmentConstants.ONI_PATH);
//		GlobalSpace space = context.getGlobalSpace();
//		Collection<User> users = space.getUsers();
		
//		ponyoContext.addUserChangeListener(UserChangeListener);
		
		JButton btnQuit = new JButton("Quit");
		final Window mainWindow = createWindow(btnQuit);
		btnQuit.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			ponyoContext.shutdown();
			mainWindow.setVisible(false);
			mainWindow.dispose(); 
		}});
		
		SwingUtilities.invokeLater(new Runnable() { public void run() {
			loadingWindow.setVisible(false);
			loadingWindow.dispose();
			mainWindow.setVisible(true);
		}});
	}
	
	private Window showLoadingWindow() {
		final JDialog window = new JDialog();
		window.setTitle("Loading PonyoNI ...");
		window.getContentPane().add(new JLabel("Loading ... one moment"));
		Dimension size = new Dimension(400, 180);
		window.setPreferredSize(size);
		window.setSize(size);
		window.setLocation(30, 30);
		window.setVisible(true);
		return window;
	}
	
	private Window createWindow(JButton btnQuit) {
		final JFrame frame = new JFrame("PnJNA - Java Sample");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());
//		panel.add(txtOutput, BorderLayout.CENTER);
		panel.add(btnQuit, BorderLayout.SOUTH);
		frame.getContentPane().add(panel);
		frame.setSize(600, 300);
		frame.setLocation(100, 60);
		return frame;
	}
}
