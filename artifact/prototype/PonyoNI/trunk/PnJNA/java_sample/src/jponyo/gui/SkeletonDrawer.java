package jponyo.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jponyo.GlobalData;
import jponyo.jna.Skel;

public class SkeletonDrawer extends JPanel {
	
	private static final long serialVersionUID = -6412556352307293613L;
	
	// TODO set fixed size, updateGraphics g2d, etc ...
	private final JLabel[] xLabels = new JLabel[Skel.COUNT];
	private final JLabel[] yLabels = new JLabel[Skel.COUNT];
	private final JLabel[] zLabels = new JLabel[Skel.COUNT];
	
	public SkeletonDrawer() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridy = 0;
		for(Skel skel : Skel.values()) {
			this.xLabels[skel.getId()] = new JLabel(" ----- ");
			this.yLabels[skel.getId()] = new JLabel(" ----- ");
			this.zLabels[skel.getId()] = new JLabel(" ----- ");
			
			c.gridx = 0;
			c.weightx = 0.5;
			c.anchor = GridBagConstraints.WEST;
			this.add(new JLabel(skel.name() + ""), c);
			
			c.weightx = 0.1;
			c.anchor = GridBagConstraints.EAST;
			c.gridx++; this.add(this.xLabels[skel.getId()], c);
			c.gridx++; this.add(new JLabel("/"), c);
			c.gridx++; this.add(this.yLabels[skel.getId()], c);
			c.gridx++; this.add(new JLabel("/"), c);
			c.gridx++; this.add(this.zLabels[skel.getId()], c);
			c.gridy++;
		}
	}
	
	public void update(GlobalData data) {
		for(Skel skel : Skel.values()) {
			final int id = skel.getId();
			this.xLabels[id].setText(String.valueOf(Math.round(data.xByJoint[id])));
			this.yLabels[id].setText(String.valueOf(Math.round(data.yByJoint[id])));
			this.zLabels[id].setText(String.valueOf(Math.round(data.zByJoint[id])));
		}
	}
}
