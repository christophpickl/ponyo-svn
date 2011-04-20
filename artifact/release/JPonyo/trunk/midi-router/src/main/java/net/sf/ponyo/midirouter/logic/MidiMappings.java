package net.sf.ponyo.midirouter.logic;

import java.util.List;

public class MidiMappings {
	
	private final List<MidiMapping> mappings;
	
	public MidiMappings(List<MidiMapping> mappings) {
		this.mappings = mappings;
	}

	public List<MidiMapping> getMappings() {
		return this.mappings;
	}
	
}
