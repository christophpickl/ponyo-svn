package net.sf.ponyo.midirouter.view;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableModel;


public class MidiMappingTable extends JTable {
	private static final long serialVersionUID = 6613765015701387044L;

	public MidiMappingTable(TableModel tableModel) {
		super(tableModel);
//		super(data, new Object[] { "Joint", "Direction" });
        this.setShowGrid(true);
        this.setGridColor(Color.LIGHT_GRAY);
        
//        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

//    @Override
//    public DataTableModel getModel() {
//    	return (DataTableModel) super.getModel();
//    }
}
