package jponyo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import jponyo.PnJNAWrapper.PnJNAWrapperListener;

public class App implements PnJNAWrapperListener {
	
	final PnJNAWrapper jna = new PnJNAWrapper();
	
	private final JTextArea text = new JTextArea();
	
	public static void main(String[] args) {
    	new App().start();
	}
	
	public void start() {
		System.out.println("App.start() START");
		
		this.initGui();
		
		this.jna.addListener(this);
		this.jna.initLib();
		this.jna.startup();
		
		System.out.println("App.start() END");
	}
	
	void onWindowClosing(JFrame window) {
		System.out.println("App.onWindowClosing(window) START");
		
		this.jna.shutdown();
		System.out.println("Hiding and disposing window");
		window.setVisible(false);
		window.dispose();
		
		System.out.println("App.onWindowClosing(window) END");
	}
	
	private void initGui() {
		final JFrame window = new JFrame();
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent event) {
				onWindowClosing(window);
			}
		});
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("App for PnJNA!!!"), BorderLayout.NORTH);
		
		this.text.setColumns(100);
		this.text.setRows(30);
		JScrollPane scrollPane = new JScrollPane(this.text);
		panel.add(scrollPane, BorderLayout.CENTER);
		window.getContentPane().add(panel);
		window.pack();
		window.setLocation(50, 50);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				System.out.println("App.SwingUtilities.run() ... displaying window");
				window.setVisible(true);
			}
		});
	}
	
	@Override public void onUserStateChanged(int userId, int userState) {
		this.text.setText(this.text.getText() + "\no User changed: ID=" + userId + "; state=" + userState);
	}

	@Override public void onJointPositionChanged(int userId, int joint, float x, float y, float z) {
		System.out.println("joint position changed!");
	}
}
