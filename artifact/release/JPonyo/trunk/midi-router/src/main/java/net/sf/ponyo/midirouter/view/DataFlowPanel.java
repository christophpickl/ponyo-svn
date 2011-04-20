package net.sf.ponyo.midirouter.view;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DataFlowPanel extends JScrollPane {

	private static final long serialVersionUID = -4943676761470458888L;
	
	public DataFlowPanel() {
		JTable table = new JTable();
		this.add(table);
	}

}
