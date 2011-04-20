package net.sf.ponyo.midirouter;

import net.sf.ponyo.jponyo.common.async.DefaultAsyncFor;
import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.jponyo.common.binding.BindingProvider;
import net.sf.ponyo.jponyo.common.binding.BindingSetter;
import net.sf.ponyo.jponyo.common.pref.PersistAsPreference;

public class Model extends DefaultAsyncFor<String, BindingListener> implements BindingProvider {
	
	public static final String MIDI_PORT = "MIDI_PORT";
	
	@PersistAsPreference
	private String midiPort;
	
//	public static final String MIDI_MAPPINGS = "MIDI_MAPPINGS";
//	FIXME @PersistAsXml
//	private String midiMappings;
	
	public String getMidiPort() {
		return this.midiPort;
	}
	
	@BindingSetter(Key = MIDI_PORT)
	public void setMidiPort(String midiPort) {
		this.midiPort = midiPort;
	}

	
	public Iterable<BindingListener> getBindingListenersFor(final String attributeKey) {
		// TODO das bekommt man auch noch raus => DefaultAsyncFor muss teilweise dafuer ein interface hergeben, wo dann aspekt getListenersFor direkt aufrufen kann!
		return this.getListenersFor(attributeKey);
	}

	// TODO diese kaskaden kann man auch noch per refelction rausbekommen! => man erbt von DefaultBindingProvider wo get/set schon definiert sind
	public final Object get(final String propertyName) {
		if(propertyName.equals(MIDI_PORT)) {
			return this.getMidiPort();
		}
		throw new RuntimeException("Unhandled property name [" + propertyName + "]!");
	}

	public final void set(final String propertyName, final Object value) {
		if(propertyName.equals(MIDI_PORT)) {
			this.setMidiPort((String) value);
		} else {
			throw new RuntimeException("Unhandled property name [" + propertyName + "]!");
		}
	}
}
