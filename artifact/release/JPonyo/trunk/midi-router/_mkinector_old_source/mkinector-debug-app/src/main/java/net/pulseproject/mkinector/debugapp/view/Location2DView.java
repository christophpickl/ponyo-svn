package net.pulseproject.mkinector.debugapp.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.pulseproject.commons.util.MathUtil;
import something.different.Util;

class Location2DView extends JPanel {
	
	private static final long serialVersionUID = -2760226484069754420L;
	
	private static final Dimension SIZE = new Dimension(180, 120);
	
	
	private final JLabel lbl = new JLabel("T");
	
	
	public Location2DView() {
		this.setBorder(BorderFactory.createLineBorder(Color.GREEN));

		this.setSize(SIZE);
		this.setMaximumSize(SIZE);
		this.setPreferredSize(SIZE);
		
		this.add(this.lbl);
	}
	
	public void updateXy(final double rawX, final double rawY) {
		final int x = Util.round3dPoint(rawX);
		final int y = Util.round3dPoint(rawY);
		
		final int x2 = MathUtil.relativateTo(20 /* sogar 0*/, 90, x, 0, this.getWidth());
		final int y2 = MathUtil.relativateTo(120 /* sogar 60*/, 250 /* sogar 270*/, y, 0, this.getHeight());
		this.lbl.setBounds(x2, y2, 10, 10);
	}
}
