package net.sf.ponyo.midirouter.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ConfigurationPanel extends JPanel {

	private static final long serialVersionUID = -1166748256589496496L;
	
	public ConfigurationPanel() {
//		model.addListenerFor(Model.MIDI_PORT, new BoundTextFieldListener(inpPort));
//		model.addListenerFor(Model.MIDI_MAPPINGS, new BoundTextFieldListener(inpMappings));
//		inpPort.addKeyListener(new ModelSettingKeyListener(model, Model.MIDI_PORT));
//		inpMappings.addKeyListener(new ModelSettingKeyListener(model, Model.MIDI_MAPPINGS));
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		final JTextField inpPort = new JTextField();
		final JTextArea inpMappings = new JTextArea();//14, 45);
		
		inpPort.setToolTipText("Enter a (receivable) MIDI Port Name");
		inpMappings.setToolTipText("Define MIDI Mappings, e.g.: 'l_hand(#torso), X, [0.0 .. 1.0 => 0 .. 127], 0, 1'");
		inpPort.setFont(Styles.FONT_MONOSPACED);
		inpMappings.setFont(Styles.FONT_MONOSPACED);
		
		final JScrollPane mappingsScrollable = new JScrollPane(inpMappings);
		inpMappings.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		mappingsScrollable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weighty = 0.0;
		c.weightx = 0.0;
		this.add(new JLabel("MIDI Port:"), c);
		
		c.gridx = 1;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(inpPort, c);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		this.add(new JLabel("MIDI Mappings:"), c);
		
		c.gridy++;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		this.add(mappingsScrollable, c);
	}
	
}
