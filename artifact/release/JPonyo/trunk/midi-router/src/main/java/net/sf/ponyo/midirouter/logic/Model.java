package net.sf.ponyo.midirouter.logic;

import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiDevice;

import net.sf.ponyo.jponyo.common.async.DefaultAsyncFor;
import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.jponyo.common.binding.BindingProvider;
import net.sf.ponyo.jponyo.common.binding.BindingSetter;
import net.sf.ponyo.jponyo.common.simplepersist.PersistAsPreference;
import net.sf.ponyo.jponyo.common.simplepersist.PersistAsXml;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Model extends DefaultAsyncFor<String, BindingListener> implements BindingProvider {
	
	private static final Log LOG = LogFactory.getLog(Model.class);
	
	public static final String MIDI_PORT = "MIDI_PORT";
	public static final String MIDI_MAPPINGS = "MIDI_MAPPINGS";
	public static final String APPLICATION_STATE = "APPLICATION_STATE";
	public static final String ACTIVE_MAPPINGS = "ACTIVE_MAPPINGS";
	public static final String FRAME_COUNT = "FRAME_COUNT";
	public static final String MIDI_DEVICES = "MIDI_DEVICES";
	
	@PersistAsPreference
	private String midiPort = "";
	
	@PersistAsXml
	private String midiMappings = "";

	private ApplicationState applicationState;
	
	private MidiMappings activeMappings;
	
	private Integer frameCount = Integer.valueOf(0);
	
	private List<MidiDevice> midiDevices = new LinkedList<MidiDevice>();
	
	private String appVersion;
	
	public Model() {
		LOG.debug("new Model()");
	}
	
	public String getMidiPort() {
		return this.midiPort;
	}
	@BindingSetter(Key = MIDI_PORT)
	public void setMidiPort(String midiPort) {
		this.midiPort = midiPort;
	}
	
	public String getMidiMappings() {
		return this.midiMappings;
	}
	@BindingSetter(Key = MIDI_MAPPINGS)
	public void setMidiMappings(String midiMappings) {
		this.midiMappings = midiMappings;
	}
	
	public ApplicationState getApplicationState() {
		return this.applicationState;
	}
	@BindingSetter(Key = APPLICATION_STATE)
	public void setApplicationState(Object applicationState) {
		this.applicationState = (ApplicationState) applicationState;
	}
	
	public MidiMappings getActiveMappings() {
		return this.activeMappings;
	}
	@BindingSetter(Key = ACTIVE_MAPPINGS)
	public void setActiveMappings(Object activeMappings) { // TODO hack: have to type mappings to object, as otherwise would have not been recognized by custom aspect :-/
//		System.out.println("setActiveMappings: " + activeMappings);
		this.activeMappings = (MidiMappings) activeMappings;
	}

	public Integer getFrameCount() {
		return this.frameCount;
	}
	@BindingSetter(Key = FRAME_COUNT)
	public void setFrameCount(Object value) {
		this.frameCount = (Integer) value;
	}

	public List<MidiDevice> getMidiDevices() {
		return this.midiDevices;
	}
	@SuppressWarnings("unchecked")
	@BindingSetter(Key = MIDI_DEVICES)
	public void setMidiDevices(Object value) {
		this.midiDevices = (List<MidiDevice>) value;
	}
	
	public String getAppVersion() {
		return this.appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	
	
	public Iterable<BindingListener> getBindingListenersFor(final String attributeKey) {
		// TODO das bekommt man auch noch raus => DefaultAsyncFor muss teilweise dafuer ein interface hergeben, wo dann aspekt getListenersFor direkt aufrufen kann!
		return this.getListenersFor(attributeKey);
	}

	// TODO diese kaskaden kann man auch noch per refelction rausbekommen! => man erbt von DefaultBindingProvider wo get/set schon definiert sind
	public final Object get(final String propertyName) {
		if(propertyName.equals(MIDI_PORT)) {
			return this.getMidiPort();
		} else if(propertyName.equals(MIDI_MAPPINGS)) {
			return this.getMidiMappings();
		} else if(propertyName.equals(APPLICATION_STATE)) {
			return this.getApplicationState();
		} else if(propertyName.equals(ACTIVE_MAPPINGS)) {
			return this.getActiveMappings();
		} else if(propertyName.equals(FRAME_COUNT)) {
			return this.getFrameCount();
		} else if(propertyName.equals(MIDI_DEVICES)) {
			return this.getMidiDevices();
		}
		throw new RuntimeException("Unhandled property name [" + propertyName + "]!");
	}

	public final void set(final String propertyName, final Object value) {
		if(propertyName.equals(MIDI_PORT)) {
			this.setMidiPort((String) value);
		} else if(propertyName.equals(MIDI_MAPPINGS)) {
			this.setMidiMappings((String) value);
		} else if(propertyName.equals(APPLICATION_STATE)) {
			this.setApplicationState((ApplicationState) value);
		} else if(propertyName.equals(ACTIVE_MAPPINGS)) {
			this.setActiveMappings((MidiMappings) value);
		} else if(propertyName.equals(FRAME_COUNT)) {
			this.setFrameCount((Integer) value);
		} else if(propertyName.equals(MIDI_DEVICES)) {
			this.setMidiDevices((List<?>) value);
		} else {
			throw new RuntimeException("Unhandled property name [" + propertyName + "]!");
		}
	}

	/** After data was loaded from pesister, we have to notify all listeners manually (pref persister injects via fields, not setter) */
	public void dispatchPersistentFieldsChange() {
		this.dispatch(MIDI_PORT, this.midiPort);
		this.dispatch(MIDI_MAPPINGS, this.midiMappings);
		
	}
	private void dispatch(String key, Object value) {
		for (BindingListener listener : this.getListenersFor(key)) {
			listener.onValueChanged(value);
		}
	}
}
