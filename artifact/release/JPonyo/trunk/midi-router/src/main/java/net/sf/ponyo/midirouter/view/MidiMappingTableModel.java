package net.sf.ponyo.midirouter.view;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.sf.ponyo.midirouter.logic.midi.MidiMapping;

public class MidiMappingTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -2428860382481347318L;
	private static final String HEADERS[] = {
		"Joint",
		"Direction",
		"MIDI",
		"Received Value",
		"Sent Value"
	};
	
	private List<MidiMapping> mappings = new LinkedList<MidiMapping>();


	public int getColumnCount() {
		return HEADERS.length;
	}

	public int getRowCount() {
		return this.mappings.size();
	}

	@Override
	public String getColumnName(int column) {
		return HEADERS[column];
	}

	@SuppressWarnings("boxing")
	public Object getValueAt(int row, int column) {
		MidiMapping map = this.mappings.get(row);
		switch(column) {
			case 0: return map.getJoint().getLabel() + (map.getRelativeToJoint() == null ? "" : " #" + map.getRelativeToJoint().getLabel());
			case 1: return map.getDirection().name();
			case 2: return "Chnl " + map.getMidiChannel() + "/ Ctrl " + map.getControllerNumber();
			case 3: return map.getRecentReceivedData();
			case 4: return map.getRecentSentData();
			default: throw new RuntimeException("column="+column);
		}
	}

	public void setMappings(List<MidiMapping> mappings) {
		this.mappings = mappings != null ? mappings : new LinkedList<MidiMapping>();
		this.fireTableDataChanged();
	}

//	@Override
//	public void setValueAt(Object value, int row, int column) {
//		this.lookup.put(new Point(row, column), value);
//	}

}
