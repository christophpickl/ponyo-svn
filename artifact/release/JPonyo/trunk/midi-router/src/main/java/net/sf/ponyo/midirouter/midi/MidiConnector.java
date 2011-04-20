package net.sf.ponyo.midirouter.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiDevice.Info;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiConnector {

	private static final Log LOG = LogFactory.getLog(MidiConnector.class);
	
	public MidiConnection loadMidiReceiver(String midiPort) throws MidiUnavailableException {
		LOG.info("loadReceiverDevice(midiPort=" + midiPort + ")");
		
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (Info info : infos) {
			if(info.getName().equals(midiPort) == false) {
				continue;
			}
			
			LOG.debug("Found MIDI device with port [" + midiPort + "].");
			MidiDevice device = MidiSystem.getMidiDevice(info);
			if(device.getMaxReceivers() != 0) {
				return new MidiConnection(device.getReceiver());
			}
		}
		
		LOG.warn("Could not find MIDI receiver device with port [" + midiPort + "]!");
		return null;
	}
}
