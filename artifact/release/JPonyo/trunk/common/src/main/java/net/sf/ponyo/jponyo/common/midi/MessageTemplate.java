package net.sf.ponyo.jponyo.common.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public abstract class MessageTemplate {
	
	private final MidiMessageStatus status;
	
	private final int data1;
	
	private final int data2;
	
	public MessageTemplate(final MidiMessageStatus status, final int data1, final int data2) {
		if(status == null) { throw new IllegalArgumentException("status == null"); }
		this.status = status;
		if(data1 < 0 || data1 > 127) { throw new IllegalArgumentException("data1 out of range [" + data1 + "]!"); }
		this.data1 = data1;
		if(data2 < 0 || data2 > 127) { throw new IllegalArgumentException("data2 out of range [" + data2 + "]!"); }
		this.data2 = data2;
	}
	
	public final ShortMessage build() {
		final ShortMessage message = new ShortMessage();
		try {
			message.setMessage(this.status.getValue(), this.data1, this.data2);
		} catch (final InvalidMidiDataException e) {
			throw new RuntimeException("Could not build MIDI message for this: " + this, e);
		}
		return message;
	}
	
	public final boolean equalsTo(final ShortMessage message) {
		return this.status.equalsTo(message) &&
			   this.data1 == message.getData1() &&
			   this.data2 == message.getData2();
	}
	
	@Override
	public final String toString() {
		return "AbstractShortMessage[status=" + this.status + ", " +
				"data1=" + this.data1 + ", data2=" + this.data2 + "]";
	}
}
