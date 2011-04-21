package net.sf.ponyo.midirouter.logic;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.AdminDialog;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.common.pref.PreferencesPersister;
import net.sf.ponyo.midirouter.logic.midi.MidiConnector;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;
import net.sf.ponyo.midirouter.logic.parser.MappingsParser;
import net.sf.ponyo.midirouter.logic.parser.ParseErrors;
import net.sf.ponyo.midirouter.view.MainView;
import net.sf.ponyo.midirouter.view.MainViewListener;
import net.sf.ponyo.midirouter.view.MidiPortsDialog;
import net.sourceforge.jpotpourri.jpotface.dialog.PtErrorDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MainPresenter
	extends DefaultAsync<MainPresenterListener>
		implements MainViewListener {
	
	private static final Log LOG = LogFactory.getLog(MainPresenter.class);
	private final static String MODEL_PREF_ID = MainPresenter.class.getName() + "-MODEL_PREF_ID";

	private final MappingsParser parser = new MappingsParser();
	private final RouterService router;
	private final Model model;
	final MainView window;
	private MidiPortsDialog midiPortsDialog;
	private AdminDialog adminDialog;
	private final PreferencesPersister persister = new PreferencesPersister();
	
	public MainPresenter(Model model, MainView window, MidiConnector midiConnector) {
		this.model = model;
		this.window = window;
		this.router = new RouterService(midiConnector);
		
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
		LOG.debug("onStartStopClicked() ... this.model.getApplicationState()=" + this.model.getApplicationState());
		
		if(this.model.getApplicationState() == ApplicationState.IDLE) {
			this.doStart();
		} else {
			this.doStop();
		}
	}
	private void doStart() {
		LOG.debug("doStart()");
		final String midiPort = this.model.getMidiPort();
		
		final MidiMappings mappings = this.parseAndSetModelMappings();
		if(mappings == null) {
			return;
		}
		
		MainPresenter.this.model.setApplicationState(ApplicationState.CONNECTING);
		new Thread(new Runnable() {
			public void run() {
				try {
					MainPresenter.this.router.start(model, midiPort, mappings);
					MainPresenter.this.model.setApplicationState(ApplicationState.RUNNING);
				} catch(Exception e) { // TODO use custom connection exception type
					LOG.warn("Startup error!", e);
					MainPresenter.this.model.setActiveMappings(null);
					MainPresenter.this.model.setApplicationState(ApplicationState.IDLE);
					Toolkit.getDefaultToolkit().beep();
					PtErrorDialog.newDialog(MainPresenter.this.window, "Startup Error", "Could not establish connection!", e).setVisible(true);
				}
			}
		}, "ConnectionStarterThread").start();
	}
	
	private MidiMappings parseAndSetModelMappings() {
		final String midiMappings = this.model.getMidiMappings();
		ParseErrors parseErrors = new ParseErrors();
		
		final MidiMappings mappings = this.parser.parseMappings(midiMappings, parseErrors);
		if(parseErrors.hasErrors() == true) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this.window, "Following error(s) occured:\n" + parseErrors.prettyPrint(), "Invalid Configuration", JOptionPane.ERROR_MESSAGE);
		} else {
			this.model.setActiveMappings(mappings);
		}
		
		return mappings;
	}
	
	private void doStop() {
		LOG.debug("doStop()");
		this.router.stop();
		
		this.model.setActiveMappings(null);
		this.model.setApplicationState(ApplicationState.IDLE);
		this.model.setFrameCount(Integer.valueOf(0));
	}

	public void onReloadClicked() {
		LOG.debug("onReloadClicked()");
		MidiMappings mappings = this.parseAndSetModelMappings();
		if(mappings != null) {
			this.router.setMappings(mappings);
		}
	}
	
	public void onQuit() {
		LOG.debug("onQuit()");
		
		if(this.midiPortsDialog != null) {
			this.midiPortsDialog.setVisible(false);
			this.midiPortsDialog.dispose();
			this.midiPortsDialog = null;
		}
		if(this.adminDialog != null) {
			this.adminDialog.setVisible(false);
			this.adminDialog.dispose();
			this.adminDialog = null;
		}
		this.window.setVisible(false);
		this.window.dispose();
		
		this.persister.persist(this.model, MODEL_PREF_ID);
		
		for (MainPresenterListener listener : this.getListeners()) {
			listener.onQuit();
		}
	}

	public void onToggleMidiPortsWindow() {
		LOG.debug("onToggleMidiPortsWindow()");
		if(this.midiPortsDialog == null) {
			this.midiPortsDialog = new MidiPortsDialog(this.model);
		}
		
		this.midiPortsDialog.setVisible(!this.midiPortsDialog.isVisible());
	}

	public void onToggleHelpWindow() {
		LOG.debug("onToggleHelpWindow()");
		Toolkit.getDefaultToolkit().beep();
	}

	public void onToggleAdminConsole() {
		LOG.debug("onToggleAdminConsole()");
		if(this.adminDialog == null) {
			this.adminDialog = new AdminDialog();
			this.router.manage(this.adminDialog);
		}
		
		this.adminDialog.setVisible(!this.adminDialog.isVisible());
		
	}
	
}
