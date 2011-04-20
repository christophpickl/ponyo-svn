package net.pulseproject.mkinector.debugapp.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.pulseproject.mkinector.josceleton.api.entity.Coordinate;
import something.different.Util;

public class CoordinatesDrawer extends JPanel {
	
	private static final long serialVersionUID = 9095756747624408284L;

	private static final int COORDINATE_COLUMNS = 3;

	private static final Dimension SIZE = new Dimension(180, 120);
	
	private final String label;
	
	private final JTextField coordinateX = new JTextField("?");
	private final JTextField coordinateY = new JTextField("?");
	private final JTextField coordinateZ = new JTextField("?");
	
	// FIXME implement feature which displays global min/max for xyz coordinates
	
	public CoordinatesDrawer(final String label) {
		this.label = label;

		this.setSize(SIZE);
		this.setMaximumSize(SIZE);
		this.setPreferredSize(SIZE);
		
//		this.setOpaque(false);
//		this.setBackground(RandomUtil.newColor());
		this.setBackground(Color.PINK);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		this.initComponents();
	}
	
	private void initComponents() {
		this.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 2;
		final JLabel lblHeader = StyleConstantsPool.newCoordinatesHeader(this.label);
		this.add(lblHeader, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		this.initCoordinateField(this.coordinateX, "x", c);

		c.gridx = 1;
		c.gridy = 1;
		this.initCoordinateField(this.coordinateY, "y", c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		this.initCoordinateField(this.coordinateZ, "z", c);
	}
	
	private void initCoordinateField(final JTextField field, final String labelText, final GridBagConstraints c) {
		field.setColumns(COORDINATE_COLUMNS);
		field.setFont(StyleConstantsPool.FONT_COORDINATE_VALUE);
		field.setOpaque(false);
		field.setEditable(false);
		field.setBorder(BorderFactory.createEmptyBorder());
		
		final JPanel labelAndFieldWrapper = new JPanel();
		labelAndFieldWrapper.setOpaque(false);
		labelAndFieldWrapper.add(StyleConstantsPool.newCoordinatesXyzLabel(labelText + ":"));
		labelAndFieldWrapper.add(field);
		
		this.add(labelAndFieldWrapper, c);
	}

	public final void updateCoordinate(final Coordinate coordinates) {
		final int[] xyz = Util.extractAndRoundXYZ(coordinates);
		this.coordinateX.setText(String.valueOf(xyz[0]));
		this.coordinateY.setText(String.valueOf(xyz[1]));
		this.coordinateZ.setText(String.valueOf(xyz[2]));
	}
	
	
}
