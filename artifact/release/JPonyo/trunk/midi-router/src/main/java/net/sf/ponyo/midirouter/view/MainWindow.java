package net.sf.ponyo.midirouter.view;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MainWindow extends JFrame {
	
	private static final Log LOG = LogFactory.getLog(MainWindow.class);
	private static final long serialVersionUID = 7350458392797569309L;
	
	public MainWindow() {
		this.setTitle("Ponyo MIDI Router");
		
		this.getContentPane().add(this.initComponent());
		this.pack();
	}
	
	private Component initComponent() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("foobar"));
		return panel;
	}
	
}
