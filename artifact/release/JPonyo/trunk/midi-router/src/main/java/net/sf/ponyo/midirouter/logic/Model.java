package net.sf.ponyo.midirouter.logic;

import net.sf.ponyo.jponyo.common.async.DefaultAsyncFor;
import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.jponyo.common.binding.BindingProvider;
import net.sf.ponyo.jponyo.common.binding.BindingSetter;
import net.sf.ponyo.jponyo.common.pref.PersistAsPreference;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;

public class Model extends DefaultAsyncFor<String, BindingListener> implements BindingProvider {
	
	public static final String MIDI_PORT = "MIDI_PORT";
	public static final String MIDI_MAPPINGS = "MIDI_MAPPINGS";
	public static final String APPLICATION_STATE = "APPLICATION_STATE";
	public static final String ACTIVE_MAPPINGS = "ACTIVE_MAPPINGS";
	
	@PersistAsPreference
	private String midiPort = "";
	
//	FIXME @PersistAsXml
	private String midiMappings = "r_hand, X, [0.0 .. 1.0 => 0 .. 127], 1, 1";

	private ApplicationState applicationState = ApplicationState.IDLE;
	
	private MidiMappings activeMappings = null;
	
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
		this.activeMappings = (MidiMappings) activeMappings;
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
		} else {
			throw new RuntimeException("Unhandled property name [" + propertyName + "]!");
		}
	}
}
