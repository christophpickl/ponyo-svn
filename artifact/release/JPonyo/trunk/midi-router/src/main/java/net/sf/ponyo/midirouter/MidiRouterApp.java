package net.sf.ponyo.midirouter;

import java.io.File;
import java.util.Properties;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.common.io.IoUtil;
import net.sf.ponyo.midirouter.logic.MainPresenter;
import net.sf.ponyo.midirouter.logic.MainPresenterListener;
import net.sf.ponyo.midirouter.logic.Model;
import net.sf.ponyo.midirouter.logic.midi.MidiConnector;
import net.sf.ponyo.midirouter.view.ImageFactory;
import net.sf.ponyo.midirouter.view.MainView;
import net.sf.ponyo.midirouter.view.framework.SplashScreen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class MidiRouterApp implements MainPresenterListener {
	
	private static final Log LOG = LogFactory.getLog(MidiRouterApp.class);

	public static final ImageFactory IMAGE_FACTORY = new ImageFactory("/image/");
	
	private MainPresenter presenter;
	
	public static void main(String[] args) {
		LOG.debug("main() START");
    	Injector injector = Guice.createInjector(new MidiRouterModule());
    	MidiRouterApp app = injector.getInstance(MidiRouterApp.class);
		app.startApplication();
		LOG.debug("main() END");
	}
	
	private void sleep(int milliseconds) { // TODO outsource in common util
		LOG.debug("sleep(milliseconds=" + milliseconds + ")");
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void startApplication() {
		LOG.info("startApplication()");
    	LOG.info("-------------------------------------");
		LOG.info("Running in Java VM: " + System.getProperty("java.version"));
		LOG.info("Execution path: " + new File("").getAbsolutePath());
    	LOG.info("-------------------------------------");
    	
		final Model model = new Model();
		
		final Properties appProperties = IoUtil.loadPropertiesFromClassPath(MidiRouterApp.class.getClassLoader(), "app.properties");
		final String applicationVersion = appProperties.get("app_version").toString();
		model.appVersion = applicationVersion;
		
		final SplashScreen splash = new SplashScreen(MidiRouterApp.IMAGE_FACTORY.getImage("splashscreen_logo.png"), "Ponyo MIDI Router v" + applicationVersion);
		splash.setLoadingMessage("Starting up ...");
		SwingUtilities.invokeLater(new Runnable() { public void run() {
				splash.setVisible(true);
		}});
//		this.sleep(500);
		
		
		MidiConnector midiConnector = new MidiConnector();
		splash.setLoadingMessage("Loading MIDI devices ...");
		model.setMidiDevices(midiConnector.loadAllDevices());
//		this.sleep(500);
		
		final MainView window =  new MainView(model);
		this.presenter = new MainPresenter(model, window, midiConnector);
		this.presenter.addListener(this);
		this.presenter.show();

		splash.setVisible(false);
		splash.dispose();
	}

	public void onQuit() {
		LOG.debug("onQuit()");
		System.exit(0); // force quit everything, just to be sure ;)
	}

}
