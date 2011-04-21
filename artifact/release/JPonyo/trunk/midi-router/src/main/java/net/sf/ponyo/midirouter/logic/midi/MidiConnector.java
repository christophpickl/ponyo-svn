package net.sf.ponyo.midirouter.logic.midi;

import java.util.List;

import javax.sound.midi.MidiDevice;

public interface MidiConnector {

	MidiConnection openConnection(String midiPort);

	List<MidiDevice> loadAllDevices();

}