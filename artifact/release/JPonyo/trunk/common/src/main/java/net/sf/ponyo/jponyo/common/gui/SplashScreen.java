/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sf.ponyo.jponyo.common.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import net.sourceforge.jpotpourri.jpotface.panel.brushed.PtBrushedMetalPanel;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SplashScreen extends JWindow {

    private static final long serialVersionUID = -1539009650852919347L;
    
    private final JLabel lblLoading = new JLabel();
    
    public SplashScreen(ImageIcon icon, String topTitle) {
        this.getContentPane().add(this.initComponents(icon, topTitle));
        this.pack();
        PtGuiUtil.setCenterLocation(this, 0, -30);
    }
    
    public void setLoadingMessage(String loadingMessage) {
    	this.lblLoading.setText(loadingMessage);
    	this.repaint();
    }
    
    private JPanel initComponents(ImageIcon icon, String topTitle) {
        final JPanel panel = new PtBrushedMetalPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        
        final JPanel panelSouth = new JPanel();
        panelSouth.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelSouth.setOpaque(false);
        
        this.lblLoading.setOpaque(false);
		panelSouth.add(this.lblLoading);
        
		JLabel lblTop = new JLabel(topTitle);
		lblTop.setOpaque(false);
		Font topFont = new Font(Font.DIALOG, Font.BOLD, 20);
		lblTop.setFont(topFont);
		panel.add(lblTop, BorderLayout.NORTH);
        panel.add(new JLabel(icon), BorderLayout.CENTER);
        panel.add(panelSouth, BorderLayout.SOUTH);

        return panel;
    }
}
