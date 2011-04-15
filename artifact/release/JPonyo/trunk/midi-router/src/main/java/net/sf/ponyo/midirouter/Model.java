package net.sf.ponyo.midirouter;

import net.sf.josceleton.core.impl.async.DefaultAsyncFor;
import net.sf.josceleton.prototype.midi.logic.bindable.BindingListener;
import net.sf.josceleton.prototype.midi.logic.bindable.BindingProvider;
import net.sf.josceleton.prototype.midi.logic.bindable.BindingSetter;
import net.sf.josceleton.prototype.midi.logic.preference.PersistAsPreference;

public class Model extends DefaultAsyncFor<String, BindingListener> implements BindingProvider {

	@PersistAsPreference public int recentWindowX;
	@PersistAsPreference public int recentWindowY;
	@PersistAsPreference public int recentWindowWidth;
	@PersistAsPreference public int recentWindowHeight;
	@PersistAsPreference public int recentDividerLocation;
	
	public static final int STATE_IDLE = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_PROCESSING = 2;
	public static final String STATE = "STATE";
	private int state = STATE_IDLE;
	public int getState() { return this.state; }
	@BindingSetter(Key = STATE) public void setState(int state) { this.state = state; }
	
	
	public static final String MIDI_PORT = "MIDI_PORT";
	@PersistAsPreference private String midiPort;
	public String getMidiPort() { return this.midiPort; }
	@BindingSetter(Key = MIDI_PORT) public void setMidiPort(String midiPort) { this.midiPort = midiPort; }
	
	public static final String MIDI_MAPPINGS = "MIDI_MAPPINGS";
	@PersistAsPreference private String midiMappings;
	
	public String getMidiMappings() { return this.midiMappings; }
	@BindingSetter(Key = MIDI_MAPPINGS) public void setMidiMappings(String midiMappings) { this.midiMappings = midiMappings; }

	public final Object get(final String propertyName) {
		if(propertyName.equals(STATE)) {
			return Integer.valueOf(this.getState());
		} else if(propertyName.equals(MIDI_PORT)) {
			return this.getMidiPort();
		} else if(propertyName.equals(MIDI_MAPPINGS)) {
			return this.getMidiMappings();
		} else {
			throw new RuntimeException("Unhandled property name [" + propertyName + "]!");
		}
	}

	public final void set(final String propertyName, final Object value) {
		if(propertyName.equals(STATE)) {
			this.setState(((Integer) value).intValue());
		} else if(propertyName.equals(MIDI_PORT)) {
			this.setMidiPort((String) value);
		} else if(propertyName.equals(MIDI_MAPPINGS)) {
			this.setMidiMappings((String) value);
		} else {
			throw new RuntimeException("Unhandled property name [" + propertyName + "]!");
		}
	}
	
	@Override public Iterable<BindingListener> getBindingListenersFor(final String attributeKey) {
		// TODO das bekommt man auch noch raus => DefaultAsyncFor muss teilweise dafuer ein interface hergeben, wo dann aspekt getListenersFor direkt aufrufen kann!
		return this.getListenersFor(attributeKey);
	}
	
}
