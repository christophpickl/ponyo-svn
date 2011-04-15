package net.sf.ponyo.jponyo.adminconsole;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.MainWindow;
import net.sf.ponyo.jponyo.adminconsole.view.MainWindowListener;
import net.sf.ponyo.jponyo.global.GlobalSpace;

public class App implements MainWindowListener {

	private final GlobalSpace data = new GlobalSpace();
	final MainWindow window = new MainWindow(this.data, this);
	
	public static void main(String[] args) {
		new App().startUp();
	}

	public void startUp() {
	    this.window.setSize(800, 600);
	    SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("displaying window");
				App.this.window.display();
			}
		});
//	    this.initJna();
	}

	public void onQuit() {
		this.window.destroy();
	}
}
