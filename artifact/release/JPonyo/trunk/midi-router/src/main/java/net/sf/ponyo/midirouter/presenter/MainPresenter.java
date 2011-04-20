package net.sf.ponyo.midirouter.presenter;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.common.gui.HtmlWindow;
import net.sf.ponyo.jponyo.common.pref.PreferencesPersister;
import net.sf.ponyo.midirouter.Model;
import net.sf.ponyo.midirouter.view.MainView;
import net.sf.ponyo.midirouter.view.MainViewListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MainPresenter
	extends DefaultAsync<MainPresenterListener>
		implements MainViewListener {
	
	private static final Log LOG = LogFactory.getLog(MainPresenter.class);
	private final static String MODEL_PREF_ID = MainPresenter.class.getName() + "-MODEL_PREF_ID";
	
	private final Model model;
	final MainView window;

	private final PreferencesPersister persister = new PreferencesPersister();
	
	public MainPresenter(Model model, MainView window) {
		this.model = model;
		this.window = window;
		this.window.addListener(this);
	}

	public void show() {
		LOG.info("show()");

		this.persister.init(this.model, MODEL_PREF_ID);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPresenter.this.onShowWindow();
			}
		});
	}
	
	void onShowWindow() {
		this.window.display(new Dimension(800, 400));
	}

//	public void onToggleHelp() {
//		if(this.helpWindow == null) {
//			this.helpWindow = new HtmlWindow("Some Window Title", "http://josceleton.sourceforge.net/static/midi-prototype-help.html?app_version=" + this.appVersion);
//			this.helpWindow.setLocation(50, 50); // MINOR set realtive to main window
//		}
//		this.helpWindow.setVisible(!this.helpWindow.isVisible());
//	}
	
	public void onStartStopClicked() {
		LOG.debug("onStartStopClicked()");
		
	}

	public void onReloadClicked() {
		LOG.debug("onReloadClicked()");
	}
	
	public void onQuit() {
		LOG.debug("onQuit()");
		
		this.window.setVisible(false);
		this.window.dispose();
		
		this.persister.persist(this.model, MODEL_PREF_ID);
		
		for (MainPresenterListener listener : this.getListeners()) {
			listener.onQuit();
		}
	}
	
}
