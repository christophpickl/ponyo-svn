package net.sf.ponyo.midirouter.refactor;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiDevice.Info;

import net.pulseproject.commons.midi.entity.ControllerMessage;
import net.pulseproject.commons.midi.entity.DeviceKind;
import net.pulseproject.commons.midi.entity.DeviceState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiConnection {
	
	private static final Log LOG = LogFactory.getLog(MidiConnection.class);
	
	private final String port;
	
	private Receiver midiReceiver;
	
	
	public MidiConnection(String port) {
		this.port = port;
	}
	
	public void connect() throws InvalidInputException {
		LOG.info("Connecting on MIDI port [" + this.port + "] ...");
		try {
			final MidiDevice device = loadDevice();
			this.midiReceiver = device.getReceiver();
			LOG.info("connect() ... FINISHED");
		} catch(InvalidInputException e) {
			throw e;
		} catch(Exception e) {
			throw new RuntimeException("midi connection error", e);
		}
	}
	
	public void send(ControllerMessage message) {
		// NO ... too many invocations ... LOG.trace("send(message=" + message + ")");
		if(this.midiReceiver == null) {
			System.err.println("Not sending MIDI message, as already closed!");
			return;
		}
		this.midiReceiver.send(message.build(), 0);
	}
	
	public void close() {
		LOG.info("Closing MIDI connection.");
		this.midiReceiver.close();
		this.midiReceiver = null;
	}
	
	private MidiDevice loadDevice() throws MidiUnavailableException, InvalidInputException {
		LOG.info("Loading MIDI devices ...");
		final Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (final Info info : infos) {
			if(info.getName().equals(this.port)) {
				final MidiDevice device = MidiSystem.getMidiDevice(info);
				final int maxCount = DeviceKind.TRANSMITTER.stateIsApplicable(DeviceState.RECEIVING_ONLY) ?
										device.getMaxTransmitters() :
										device.getMaxReceivers();
				if(maxCount != 0) {
					return device;
				}
			}
		}
		throw InvalidInputException.newInvalidMidiPort(this.port);
	}
}
