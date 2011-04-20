package net.sf.ponyo.midirouter.midi;

import javax.sound.midi.Receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiConnection {
	
	private static final Log LOG = LogFactory.getLog(MidiConnection.class);
	
	private final Receiver receiver;
	
	public MidiConnection(Receiver receiver) {
		this.receiver = receiver;
	}
	
	public void send() {
		// FIXME implement me
	}
	
	public void close() {
		LOG.debug("close()");
		this.receiver.close();
	}

}
