package net.sf.ponyo.jponyo.adminconsole.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.sf.ponyo.jponyo.core.GlobalSpace;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.stream.MotionData;

public class SkeletonNumberPanel extends JPanel {
	
	private static final long serialVersionUID = -6412556352307293613L;

	private JTable table;
	
	public SkeletonNumberPanel() {
		Object[][] tableData = new Object[Joint.MAX_JOINT_ID_PLUS_ONE][4];
		for(Joint joint : Joint.values()) {
			tableData[joint.getId()] = new String[] { joint.getLabel(), "X", "X", "X" };
		}
		
		String[] columns = new String[] { "Joint", "X", "Y", "Z" };
		TableModel dm = new DefaultTableModel(tableData, columns);
		this.table = new JTable(dm);
		this.add(new JScrollPane(this.table));
	}
	
	public void update(MotionData data) {
		Joint joint = data.getJoint();
		float[] position = data.getJointPosition().data;
		
		for (int i = 0; i < position.length; i++) {
			this.table.setValueAt(String.valueOf(Math.round(position[i])), joint.getId(), i+1);
		}
	}
}
