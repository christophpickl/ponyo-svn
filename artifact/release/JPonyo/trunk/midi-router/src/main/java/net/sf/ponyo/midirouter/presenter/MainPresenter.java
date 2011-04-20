package net.sf.ponyo.midirouter.presenter;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.midirouter.view.MainView;
import net.sf.ponyo.midirouter.view.framework.AbstractMainViewListener;

public class MainPresenter
	extends DefaultAsync<MainPresenterListener>
		implements AbstractMainViewListener {
	
	private static final Log LOG = LogFactory.getLog(MainPresenter.class);
	
	final MainView window;

	public MainPresenter(MainView window) {
		this.window = window;
		this.window.addListener(this);
	}

	public void show() {
		LOG.info("show()");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPresenter.this.onShowWindow();
			}
		});
	}
	
	void onShowWindow() {
		this.window.display(new Dimension(800, 400));
	}
	
	public void onQuit() {
		LOG.debug("onQuit()");
		this.window.setVisible(false);
		this.window.dispose();
		
		for (MainPresenterListener listener : this.getListeners()) {
			listener.onQuit();
		}
	}
	
}
