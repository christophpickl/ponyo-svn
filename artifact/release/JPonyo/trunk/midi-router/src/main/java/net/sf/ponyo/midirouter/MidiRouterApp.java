package net.sf.ponyo.midirouter;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.midirouter.view.MainWindow;

public class MidiRouterApp {
	
	private static final Log LOG = LogFactory.getLog(MidiRouterApp.class);
	
	public static void main(String[] args) {
		new MidiRouterApp().startApplication();
	}
	
	public void startApplication() {
		LOG.info("startApplication()");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				onShowMainWindow();
			}
		});
	}

	void onShowMainWindow() {
		LOG.info("onShowMainWindow()");
		MainWindow window = new MainWindow();
		window.setVisible(true);
	}
}
