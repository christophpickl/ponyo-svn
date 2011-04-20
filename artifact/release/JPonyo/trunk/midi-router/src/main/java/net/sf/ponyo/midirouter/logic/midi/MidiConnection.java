package net.sf.ponyo.midirouter.logic.midi;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import net.sf.ponyo.jponyo.common.async.Closeable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiConnection implements Closeable {
	
	private static final Log LOG = LogFactory.getLog(MidiConnection.class);
	
	private final Receiver receiver;
	
	public MidiConnection(Receiver receiver) {
		this.receiver = receiver;
	}
	
	public void send(ShortMessage message) {
//		LOG.trace("send(message="+MidiUtil.toString(message)+")");
		this.receiver.send(message, 0);
	}
	
	public void close() {
		LOG.debug("close()");
		this.receiver.close();
	}

}
