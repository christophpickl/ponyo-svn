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

package net.sf.ponyo.midirouter.view.framework;

import java.awt.BorderLayout;

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
    
    
    public SplashScreen(ImageIcon icon, String loadingMessage) {
        this.getContentPane().add(this.initComponents(icon, loadingMessage));
        this.pack();
        PtGuiUtil.setCenterLocation(this, 0, -30);
    }
    
    private JPanel initComponents(ImageIcon icon, String loadingMessage) {
        final JPanel panel = new PtBrushedMetalPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        
        final JPanel panelSouth = new JPanel();
        panelSouth.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelSouth.setOpaque(false);
        panelSouth.add(new JLabel(loadingMessage));
        
        panel.add(new JLabel(icon), BorderLayout.CENTER);
        panel.add(panelSouth, BorderLayout.SOUTH);

        return panel;
    }
}
