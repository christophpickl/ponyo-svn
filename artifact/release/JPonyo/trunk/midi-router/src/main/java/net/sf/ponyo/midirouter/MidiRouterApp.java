package net.sf.ponyo.midirouter;

import java.io.File;
import java.util.Properties;

import javax.swing.SwingUtilities;

import net.sf.ponyo.console.gl.GLUtil;
import net.sf.ponyo.jponyo.common.gui.SplashScreen;
import net.sf.ponyo.jponyo.common.io.IoUtil;
import net.sf.ponyo.jponyo.common.simplepersist.SimplePersister;
import net.sf.ponyo.jponyo.common.simplepersist.SimplePersisterImpl;
import net.sf.ponyo.midirouter.logic.ApplicationState;
import net.sf.ponyo.midirouter.logic.MainPresenter;
import net.sf.ponyo.midirouter.logic.MainPresenterListener;
import net.sf.ponyo.midirouter.logic.Model;
import net.sf.ponyo.midirouter.logic.midi.MidiConnector;
import net.sf.ponyo.midirouter.view.ImageFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class MidiRouterApp implements MainPresenterListener {

	static {
		GLUtil.ensureJoglLibs();
	}
	
	private static final Log LOG = LogFactory.getLog(MidiRouterApp.class);

	public static final ImageFactory IMAGE_FACTORY = new ImageFactory("/image/");
	
	private final MainPresenter presenter;
	private final MidiConnector midiConnector;
	private final Model model;
	
	private final static String MODEL_PREF_ID = MidiRouterApp.class.getName() + "-MODEL_PREF_ID";
	private final SimplePersister persister = new SimplePersisterImpl(new File("target/ponyo_midirouter_data"));
	
	public static void main(String[] args) {
		LOG.debug("main() START");

		final SplashScreen splash = new SplashScreen(MidiRouterApp.IMAGE_FACTORY.getImage("splashscreen_logo.png"), "Ponyo MIDI Router");
		splash.setLoadingMessage("Starting up ...");
		SwingUtilities.invokeLater(new Runnable() { public void run() {
				splash.setVisible(true);
		}});
		
    	Injector injector = Guice.createInjector(new MidiRouterModule());
    	MidiRouterApp app = injector.getInstance(MidiRouterApp.class);
		app.startApplication(splash);
		LOG.debug("main() END");
	}
	
	
	@Inject
	public MidiRouterApp(MainPresenter presenter, MidiConnector midiConnector, Model model) {
		this.midiConnector = midiConnector;
		this.presenter = presenter;
		this.model = model;
	}


	private void sleep(int milliseconds) { // TODO outsource in common util
		LOG.debug("sleep(milliseconds=" + milliseconds + ")");
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void startApplication(SplashScreen splash) {
		LOG.info("startApplication()");
    	LOG.info("-------------------------------------");
		LOG.info("Running in Java VM: " + System.getProperty("java.version"));
		LOG.info("Execution path: " + new File("").getAbsolutePath());
    	LOG.info("-------------------------------------");

		final Properties appProperties = IoUtil.loadPropertiesFromClassPath(MidiRouterApp.class.getClassLoader(), "app.properties");
		final String applicationVersion = appProperties.get("app_version").toString();
		this.model.setAppVersion(applicationVersion);
		
		splash.setLoadingMessage("Loading MIDI devices ...");
		this.model.setMidiDevices(this.midiConnector.loadAllDevices());

		splash.setLoadingMessage("Preparing user interface.");
		this.sleep(100);
		this.persister.init(this.model, MODEL_PREF_ID);
		this.model.dispatchPersistentFieldsChange();
		this.model.setApplicationState(ApplicationState.IDLE);
		if(this.model.getMidiMappings().isEmpty()) {
			this.model.setMidiMappings("r_hand, X, [-300.0 .. 500.0 => 0 .. 127], 1, 1");
		}

		splash.setLoadingMessage("Starutp done.");
		this.sleep(100);

		splash.setVisible(false);
		splash.dispose();
		this.presenter.addListener(this);
		this.presenter.show();
	}

	public void onQuit() {
		LOG.debug("onQuit()");
		
		this.persister.persist(this.model, MODEL_PREF_ID);
		
		System.exit(0); // force quit everything, just to be sure ;)
	}

}
