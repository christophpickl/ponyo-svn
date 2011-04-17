package net.sf.ponyo.jponyo.adminconsole.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.core.GlobalSpace;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.entity.Skeleton;
import net.sf.ponyo.jponyo.user.User;

public class SkeletonNumberPanel extends JPanel {
	
	private static final long serialVersionUID = -6412556352307293613L;
	
	// TODO set fixed size, updateGraphics g2d, etc ...
	private final JLabel[] xLabels = new JLabel[Joint.MAX_JOINT_ID_PLUS_ONE];
	private final JLabel[] yLabels = new JLabel[Joint.MAX_JOINT_ID_PLUS_ONE];
	private final JLabel[] zLabels = new JLabel[Joint.MAX_JOINT_ID_PLUS_ONE];
	
	private final GlobalSpace space;
	
	public SkeletonNumberPanel(GlobalSpace space) {
		this.space = space;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridy = 0;
		for(Joint joint : Joint.values()) {
			this.xLabels[joint.getId()] = new JLabel(" ----- ");
			this.yLabels[joint.getId()] = new JLabel(" ----- ");
			this.zLabels[joint.getId()] = new JLabel(" ----- ");
			
			c.gridx = 0;
			c.weightx = 0.5;
			c.anchor = GridBagConstraints.WEST;
			this.add(new JLabel(joint.getLabel()), c);
			
			c.weightx = 0.1;
			c.anchor = GridBagConstraints.EAST;
			c.gridx++; this.add(this.xLabels[joint.getId()], c);
			c.gridx++; this.add(new JLabel("/"), c);
			c.gridx++; this.add(this.yLabels[joint.getId()], c);
			c.gridx++; this.add(new JLabel("/"), c);
			c.gridx++; this.add(this.zLabels[joint.getId()], c);
			c.gridy++;
		}
	}
	
	public void update() {
		Collection<User> users = this.space.getUsers();
		if(users.isEmpty()) {
			return;
		}
		User user = users.iterator().next();
		Skeleton skel = user.getSkeleton();
		
		for(Joint joint : Joint.values()) {
			final int id = joint.getId();
			float[] coord = skel.getCoordinates(joint).data;
			this.xLabels[id].setText(String.valueOf(coord[0]));
			this.yLabels[id].setText(String.valueOf(coord[1]));
			this.zLabels[id].setText(String.valueOf(coord[2]));
		}
	}
}
