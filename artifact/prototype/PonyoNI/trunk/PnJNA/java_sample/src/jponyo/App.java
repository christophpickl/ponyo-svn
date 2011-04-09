package jponyo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class App {
	private final PnJNAWrapper jna = new PnJNAWrapper();
	
	public static void main(String[] args) {
    	new App().start();
	}
	
	public void start() {
		System.out.println("App.start() START");
		
		final JFrame window = new JFrame();
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent event) {
				System.out.println("App.window.windowClosing()");
				App.this.jna.shutdown();
				window.setVisible(false);
				window.dispose();
			}
		});
		JPanel panel = new JPanel();
		panel.add(new JLabel("App for PnJNA!!!"));
		window.getContentPane().add(panel);
		window.pack();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				System.out.println("App.SwingUtilities.run() ... displaying window");
				window.setVisible(true);
			}
		});
		
		this.jna.initLib();
		this.jna.startup();
		System.out.println("App.start() END");
	}
}
