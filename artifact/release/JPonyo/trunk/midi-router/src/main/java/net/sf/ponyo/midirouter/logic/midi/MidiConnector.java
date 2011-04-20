package net.sf.ponyo.midirouter.logic.midi;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiDevice.Info;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiConnector {

	private static final Log LOG = LogFactory.getLog(MidiConnector.class);
	
	private List<MidiDevice> deviceCache;
	
	public MidiConnection openConnection(String midiPort) {
		LOG.info("openConnection(midiPort=" + midiPort + ")");
		this.initCache();
		
		for (MidiDevice device : this.deviceCache) {
			Info info = device.getDeviceInfo();
			if(info.getName().equals(midiPort) == false) {
				continue;
			}
			
			int maxReceivers = device.getMaxReceivers();
			LOG.debug("Found MIDI device with port [" + midiPort + "] and maxReceivers [" + maxReceivers + "].");
			if(maxReceivers  != 0) {
				try {
					return new MidiConnection(device.getReceiver());
				} catch (MidiUnavailableException e) {
					LOG.warn("Could not get MIDI receiver for device: " + info, e);
					return null;
				}
			}
		}
		
		LOG.warn("Could not find MIDI receiver device with port [" + midiPort + "]!");
		return null;
	}

	public List<MidiDevice> loadAllDevices() {
		LOG.debug("loadAllDevices()");
		this.initCache();
		return this.deviceCache;
	}
	
	private void initCache() {
		if(this.deviceCache != null) {
			return;
		}
		LOG.debug("initCache() ... creating cache");
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		this.deviceCache = new ArrayList<MidiDevice>(infos.length);
		for (Info info : infos) {
			MidiDevice device;
			try {
				device = MidiSystem.getMidiDevice(info);
			} catch (MidiUnavailableException e) {
				throw new RuntimeException("Could not get MIDI device for info: " + info, e);
			}
			this.deviceCache.add(device);
		}
	}
}
