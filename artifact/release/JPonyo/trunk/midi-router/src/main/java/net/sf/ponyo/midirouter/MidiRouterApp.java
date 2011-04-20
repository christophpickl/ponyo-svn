package net.sf.ponyo.midirouter;

import net.sf.ponyo.midirouter.logic.MainPresenter;
import net.sf.ponyo.midirouter.logic.MainPresenterListener;
import net.sf.ponyo.midirouter.logic.Model;
import net.sf.ponyo.midirouter.view.ImageFactory;
import net.sf.ponyo.midirouter.view.MainView;
import net.sf.ponyo.midirouter.view.framework.SplashScreen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiRouterApp implements MainPresenterListener {
	
	private static final Log LOG = LogFactory.getLog(MidiRouterApp.class);

	public static final ImageFactory IMAGE_FACTORY = new ImageFactory("/image/");
	
	private MainPresenter presenter;
	
	public static void main(String[] args) {
		SplashScreen splash = new SplashScreen(IMAGE_FACTORY.getImage("splashscreen_logo.png"), "Ponyo MIDI Router starting up ...");
		splash.setVisible(true);
		
		new MidiRouterApp().startApplication();
		
		splash.setVisible(false);
		splash.dispose();
	}
	
	public void startApplication() {
		LOG.info("startApplication()");
		final Model model = new Model();
		final MainView window =  new MainView(model);
		this.presenter = new MainPresenter(model, window);
		this.presenter.addListener(this);
		this.presenter.show();
	}

	public void onQuit() {
		LOG.debug("onQuit()");
		// TODO destroy connection, etc
	}

}
