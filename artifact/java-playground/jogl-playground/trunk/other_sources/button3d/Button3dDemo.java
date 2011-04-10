package org.progx.button3d;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.opengl.utils.Animator;

public class Button3dDemo extends JFrame {
    public Button3dDemo() throws HeadlessException {
        super("Button 3D Demo");
        
        JButton button = new JButton();
        
        JPanel pane = new JPanel(new FlowLayout());
        pane.setOpaque(false);
        pane.add(build3dIcon(button));
        pane.add(new JLabel("Preferences"));

        button.setLayout(new GridBagLayout());
        button.setMargin(new Insets(3, 3, 3, 3));
        button.add(pane, new GridBagConstraints(1, 0, 1, 1,
                                                0.0, 0.0,
                                                GridBagConstraints.CENTER,
                                                GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0),
                                                0, 0));

        add(button);
        
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Component build3dIcon(JButton button) {
        GearsPanel panel = new GearsPanel();
        final Animator animator = new Animator(panel);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                animator.stop();
            }
        
            @Override
            public void mouseEntered(MouseEvent e) {
                animator.start();
            }
        });
        
        panel.setPreferredSize(new Dimension(64, 64));
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Button3dDemo demo = new Button3dDemo();
                demo.setVisible(true);
            }
        });
    }
}
