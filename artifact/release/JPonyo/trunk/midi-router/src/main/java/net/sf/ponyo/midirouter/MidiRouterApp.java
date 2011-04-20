package net.sf.ponyo.midirouter;

import javax.swing.SwingUtilities;

import net.sf.ponyo.midirouter.view.MainWindow;

public class MidiRouterApp {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainWindow().setVisible(true);
			}
		});
	}
	
}
