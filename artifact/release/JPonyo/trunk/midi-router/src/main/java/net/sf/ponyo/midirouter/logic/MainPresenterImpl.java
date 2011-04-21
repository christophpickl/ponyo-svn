package net.sf.ponyo.midirouter.logic;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.AdminDialog;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;
import net.sf.ponyo.midirouter.logic.parser.MappingsParser;
import net.sf.ponyo.midirouter.logic.parser.ParseErrors;
import net.sf.ponyo.midirouter.view.MainView;
import net.sf.ponyo.midirouter.view.MainViewListener;
import net.sf.ponyo.midirouter.view.MidiPortsDialog;
import net.sf.ponyo.midirouter.view.MidiPortsDialogProvider;
import net.sourceforge.jpotpourri.jpotface.dialog.PtErrorDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;

class MainPresenterImpl
	extends DefaultAsync<MainPresenterListener>
		implements MainViewListener, MainPresenter {
	
	private static final Log LOG = LogFactory.getLog(MainPresenterImpl.class);

	private final Model model;
	private final RouterService router;
	private final MidiPortsDialogProvider midiDialogProvider;
	private final MappingsParser parser = new MappingsParser();
	
	final MainView window;
	private MidiPortsDialog midiPortsDialog;
	private AdminDialog adminDialog;
	
	@Inject
	public MainPresenterImpl(Model model, MainView window, RouterService router, MidiPortsDialogProvider midiDialogProvider) {
		this.model = model;
		this.window = window;
		this.router = router;
		this.midiDialogProvider = midiDialogProvider;
		
		this.window.addListener(this);
	}

	public void show() {
		LOG.info("show()");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPresenterImpl.this.onShowWindow();
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
		
		new Thread(new Runnable() {
			public void run() {
				onThreadRunStartApplication();
			}
		}, "ConnectionStarterThread").start();
	}
	
	void onThreadRunStartApplication() {
		try {
			this.model.setApplicationState(ApplicationState.CONNECTING);
			
			final MidiMappings mappings = this.parseAndSetModelMappings();
			if(mappings == null) {
				return;
			}
			
			this.router.start();
			this.model.setApplicationState(ApplicationState.RUNNING);
			
		} catch(Exception e) { // TODO use custom connection exception type
			LOG.warn("Startup error!", e);
			
			this.model.setActiveMappings(null);
			this.model.setApplicationState(ApplicationState.IDLE);
			
			Toolkit.getDefaultToolkit().beep();
			
			String errorMessage = "Could not establish connection!\n";
			if(e instanceof NullPointerException) {
				errorMessage += "Null Pointer Exception!";
			} else if(e.getMessage() != null && e.getMessage().trim().isEmpty() == false) {
				errorMessage += e.getMessage();
			} else {
				errorMessage += "See stacktrace for details.";
			}
			PtErrorDialog.newDialog(MainPresenterImpl.this.window.asJFrame(), "Startup Error", errorMessage, e).setVisible(true);
		}
	}
	
	private MidiMappings parseAndSetModelMappings() {
		final String midiMappings = this.model.getMidiMappings();
		ParseErrors parseErrors = new ParseErrors();
		
		final MidiMappings mappings = this.parser.parseMappings(midiMappings, parseErrors);
		if(parseErrors.hasErrors() == true) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this.window.asJFrame(), "Following error(s) occured:\n" + parseErrors.prettyPrint(), "Invalid Configuration", JOptionPane.ERROR_MESSAGE);
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
			this.router.updateMappings();
		}
	}

	public void onToggleMidiPortsWindow() {
		LOG.debug("onToggleMidiPortsWindow()");
		if(this.midiPortsDialog == null) {
			this.midiPortsDialog = this.midiDialogProvider.get();
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
		
		for (MainPresenterListener listener : this.getListeners()) {
			listener.onQuit();
		}
	}
	
}
