package net.pulseproject.mkinector.debugapp.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.pulseproject.mkinector.debugapp.misc.OscConnectionWindowGlueListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MainWindow extends JFrame implements OscConnectionWindowGlueListener {
	
	private static final Log LOG = LogFactory.getLog(MainWindow.class);
	
	private static final long serialVersionUID = -5020144064913345770L;
	

	private final MainWindowListener listener;

	private final MainPanel mainPanel = new MainPanel();
	
	
	public MainWindow(final MainWindowListener listener) {
		super("Kinector Debugger");
		this.listener = listener;
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				MainWindow.this.onQuit();
			}
		});

		this.getContentPane().add(this.mainPanel);
		this.pack();
//		this.setSize(940, 850);
	}
	
	private void onQuit() {
		LOG.info("onQuit()");
		
		this.listener.onQuit();
		this.dispose();
	}

	@Override
	public final void onAddUserPanel(final UserPanel userPanel) {
		this.mainPanel.onAddUserPanel(userPanel);
		
		this.pack();
	}

	@Override
	public final void onRemoveUserPanel(final UserPanel userPanel) {
		this.mainPanel.onRemoveUserPanel(userPanel);
		
		this.pack();
	}

	@Override
	public final void onUserCountChanged(final int userReadyCount, final int userWaitingCount) {
		this.mainPanel.onUserCountChanged(userReadyCount, userWaitingCount);
	}

}
