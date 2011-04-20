package net.sf.ponyo.midirouter.logic.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiDevice.Info;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiConnector {

	private static final Log LOG = LogFactory.getLog(MidiConnector.class);
	
	public MidiConnection openConnection(String midiPort) throws MidiUnavailableException {
		LOG.info("openConnection(midiPort=" + midiPort + ")");
		
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (Info info : infos) {
			if(info.getName().equals(midiPort) == false) {
				continue;
			}
			
			MidiDevice device = MidiSystem.getMidiDevice(info);
			int maxReceivers = device.getMaxReceivers();
			LOG.debug("Found MIDI device with port [" + midiPort + "] and maxReceivers [" + maxReceivers + "].");
			if(maxReceivers  != 0) {
				return new MidiConnection(device.getReceiver());
			}
		}
		
		LOG.warn("Could not find MIDI receiver device with port [" + midiPort + "]!");
		return null;
	}
}
