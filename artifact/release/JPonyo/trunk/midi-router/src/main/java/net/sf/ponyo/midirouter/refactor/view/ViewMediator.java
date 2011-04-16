package net.sf.ponyo.midirouter.refactor.view;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import net.sf.ponyo.jponyo.common.gui.HtmlWindow;
import net.sf.ponyo.jponyo.common.pref.PreferencesPersister;
import net.sf.ponyo.midirouter.Model;
import net.sf.ponyo.midirouter.refactor.LogUtil;
import net.sf.ponyo.midirouter.refactor.MappingsParser;
import net.sf.ponyo.midirouter.refactor.MidiMapping;
import net.sf.ponyo.midirouter.refactor.PrototypeLogic;
import net.sf.ponyo.midirouter.refactor.SomeUtil;

import com.google.inject.Injector;

public class ViewMediator implements MainWindowListener {
	
	private final static String MODEL_PREF_ID = "MyMainWindow";

//	private ConsoleWindow console;
	
	private PrototypeLogic recentLogic;
	
	private final PreferencesPersister persister = new PreferencesPersister();
	
	private final Model model;
	
	private HtmlWindow helpWindow;
	private final String appVersion;
	
	private Injector consoleInjector;
//	private final ConsolePresenterFactory consolePresenterFactory;
	
	public ViewMediator(String appVersion, final Model model) {
		this.appVersion = appVersion;
		this.model = model;
		
		this.persister.init(this.model, MODEL_PREF_ID);
		
		String midiPort = this.model.getMidiPort();
		if(midiPort == null) {
			midiPort = "IAC Driver - XXX";
		}
		this.model.setMidiPort(midiPort);
		
		String midiMappings = this.model.getMidiMappings();
		if(midiMappings == null) {
			midiMappings =	"# Format for each line:\n" +
							"#   <JOINT>, <XYZ>, <MIDI-CH>, <CTRL-NR>\n" +
							"\n" +
							"l_hand, X, 0, 1\n" +
							"r_hand, Y, 0, 2";
		}
		this.model.setMidiMappings(midiMappings);

		// FIXME
//		this.consoleInjector = Guice.createInjector(new AbstractModule() {
//			@Override protected void configure() {
//				install(new ConsolePrototypeModule());
//			}
//		});
//		this.consolePresenterFactory = consoleInjector.getInstance(ConsolePresenterFactory.class);
	}
	
	@SuppressWarnings("synthetic-access")
	private void doStart() {
		final String port = this.model.getMidiPort();
		new Thread(new Runnable() {
			public void run() {
				try {
					ViewMediator.this.model.setState(Model.STATE_CONNECTING);
					LogUtil.clearLog();
					
					Collection<MidiMapping> mappings = tryToConvert();
					if(mappings == null) {
						return;
					}
					LogUtil.log("");

					LogUtil.log("Opening connection ...");
					ViewMediator.this.recentLogic = new PrototypeLogic(port, mappings);
					ViewMediator.this.recentLogic.open();
					ViewMediator.this.model.setState(Model.STATE_PROCESSING);
					LogUtil.log("Connection established (displaying every " + LogUtil.LOG_JOINT_EVERY +"th messages).");
					LogUtil.log("");
					
				} catch (Exception e) {
					ViewMediator.this.model.setState(Model.STATE_IDLE);
					SomeUtil.handleException(e);
				}
			}
		}).start();
	}
	public void onReloadMidiMappings() {
		LogUtil.log("Reloading MIDI Mappings configuration ...");
		Collection<MidiMapping> mappings = tryToConvert();
		if(mappings == null) {
			return;
		}
		this.recentLogic.updateMappings(mappings);
	}
	
	private Collection<MidiMapping> tryToConvert() {
		final String rawMappings = this.model.getMidiMappings();
		final MidiMapping[] mappingsArray;
//		try {
			mappingsArray = MappingsParser.parseMappings(rawMappings);
//		} catch (InvalidInputException e) {
//			this.model.setState(Model.STATE_IDLE);
//			JOptionPane.showMessageDialog(null, e.getMessage(), "Configuration Error", JOptionPane.WARNING_MESSAGE);
////			LogUtil.log("WARNING: Invalid input: " + e.getMessage());
//			return null;
//		}
		
		Collection<MidiMapping> mappings = new LinkedList<MidiMapping>(Arrays.asList(mappingsArray));
		LogUtil.log("Successfully parsed " + mappings.size()+ " MIDI mapping(s):");
		LogUtil.log("");
		int i = 1;
		for (final MidiMapping map : mappings) {
			LogUtil.log("  "+(i++)+". " + map);
		}
		LogUtil.log("");
		
		return mappings;
	}
	public void onToggleStartStop() {
		int state = this.model.getState();
		if(state == Model.STATE_PROCESSING) {
			this.doStop();
		} else if(state == Model.STATE_IDLE) {
			this.doStart();
		} else {
			throw new RuntimeException("Unhandled state: " + state);
		}
	}
	private void doStop() {
		this.model.setState(Model.STATE_IDLE);
		try {
//			if(this.console != null) {
//				this.console.dispose();
//				this.console = null;
//			}
			if(this.recentLogic != null) {
				this.recentLogic.close();
				this.recentLogic = null;
			}
			LogUtil.log("Connections closed.");
		} catch (Exception e) {
			SomeUtil.handleException(e);
		}
	}

	public void onQuit() {
		try {
			this.doStop();
			this.persister.persist(this.model, MODEL_PREF_ID);
		} catch (Exception e) {
			SomeUtil.handleException(e);
		}
	}
	
	public void onToggleConsole() {
//		if(this.recentLogic == null) {
//			Toolkit.getDefaultToolkit().beep();
//			return;
//		}
//		
//		if(console == null) {
//			this.console = consoleInjector.getInstance(ConsoleWindow.class);
//			if(this.recentLogic.getJoscConnection() == null) {
//				System.err.println("waui");
//			}
//			final ConsolePresenter consolePresenter = this.consolePresenterFactory.create(console, this.recentLogic.getJoscConnection());
//			consolePresenter.init();
//		}
//		
//		console.setVisible(!console.isVisible());
	}
	
	public void onToggleHelp() {
		if(this.helpWindow == null) {
			this.helpWindow = new HtmlWindow("Some Window Title", "http://josceleton.sourceforge.net/static/midi-prototype-help.html?app_version=" + this.appVersion);
			this.helpWindow.setLocation(50, 50); // MINOR set realtive to main window
		}
		this.helpWindow.setVisible(!this.helpWindow.isVisible());
	}
	
}
