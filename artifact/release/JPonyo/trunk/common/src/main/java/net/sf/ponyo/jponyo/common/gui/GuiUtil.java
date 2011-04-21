package net.sf.ponyo.jponyo.common.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

/**
 * @since 0.4
 */
public final class GuiUtil {

	private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    

    private GuiUtil() {
    	// util classes should not be instantiated
    }

    /**
     * @since 0.4
     */
    public static void setCenterLocation(final Component component) {
    	GuiUtil.setCenterLocation(component, 0, 0);
    }

    /**
     * @since 0.4
     */
    public static void setCenterLocation(final Component component, final int xOffset, final int yOffset) {
        if(component == null) { throw new IllegalArgumentException("component == null"); }
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        final int x = (screenSize.width - component.getWidth()) / 2;
        final int y = (screenSize.height - component.getHeight()) / 2;

        component.setLocation(x + xOffset, y + yOffset);
    }

    /**
     * @since 0.4
     */
    @SuppressWarnings("synthetic-access")
    public static void enableHandCursor(final Component component) {
        component.addMouseListener(new MouseAdapter() {
			@Override public void mouseEntered(final MouseEvent event) {
                component.setCursor(GuiUtil.HAND_CURSOR);
            }
            @Override public void mouseExited(final MouseEvent event) {
                component.setCursor(GuiUtil.DEFAULT_CURSOR);
            }
        });
    }

    /**
     * @since 0.4
     */
    public static JLabel newJLabel(final String text, final Font font) {
        return GuiUtil.newJLabel(text, font, null);
    }

    /**
     * @since 0.4
     */
    public static JLabel newJLabel(final String text, final Font font, final Color fontColor) {
        final JLabel lbl = new JLabel(text);
        
        lbl.setFont(font);
        if(fontColor != null) {
                lbl.setForeground(fontColor);
        }
        
        return lbl;
    }

	
}
