package net.pulseproject.mkinector.debugapp.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import net.pulseproject.commons.util.GuiUtil;

public final class StyleConstantsPool {
	
	private StyleConstantsPool() {
		// not instantiable
	}

	private static final String FONT_NAME = Font.MONOSPACED;
	
	public static final Font FONT_COORDINATE_VALUE = new Font(FONT_NAME, Font.BOLD, 30);
	public static final Font FONT_MAIN_INFO_TEXT = new Font(FONT_NAME, Font.PLAIN, 22);
	private static final Font FONT_USER_PANEL_STATE = new Font(FONT_NAME, Font.ITALIC, 18);
	
	private static final Font FONT_COORDINATES_XYZ = new Font(FONT_NAME, Font.PLAIN, 14);
	private static final Font FONT_COORDINATES_HEADER = new Font(FONT_NAME, Font.BOLD, 18);
	
	private static final Color LIGHT_FONT = new Color(0x333333);
	
	
	public static JLabel newCoordinatesHeader(final String label) {
		final JLabel lbl = GuiUtil.newJLabel(label, StyleConstantsPool.FONT_COORDINATES_HEADER);
		lbl.setForeground(LIGHT_FONT);
		return lbl;
	}
	
	public static JLabel newCoordinatesXyzLabel(final String label) {
		final JLabel lbl = GuiUtil.newJLabel(label, StyleConstantsPool.FONT_COORDINATES_XYZ);
		lbl.setForeground(LIGHT_FONT);
		return lbl;
	}

	public static JLabel newUserPanelTopLabel(final String label) {
		final JLabel lbl = GuiUtil.newJLabel(label, StyleConstantsPool.FONT_MAIN_INFO_TEXT);
		lbl.setForeground(Color.WHITE);
		return lbl;
	}
	public static JLabel newUserPanelStateLabel(final String label) {
		final JLabel lbl = GuiUtil.newJLabel(label, StyleConstantsPool.FONT_USER_PANEL_STATE);
		lbl.setForeground(Color.WHITE);
		return lbl;
	}
	
}
