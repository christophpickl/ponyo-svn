package net.sf.ponyo.midirouter.refactor.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.sf.josceleton.prototype.midi.Model;

public class ConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 7313613687940417715L;

	public ConfigurationPanel(final Model model, final MainPanelListener listener) {
		final JTextField inpPort = new JTextField(50);
		final JTextArea inpMappings = new JTextArea(14, 45);

		model.addListenerFor(Model.MIDI_PORT, new BoundTextFieldListener(inpPort));
		model.addListenerFor(Model.MIDI_MAPPINGS, new BoundTextFieldListener(inpMappings));
		inpPort.addKeyListener(new Foo(model, Model.MIDI_PORT));
		inpMappings.addKeyListener(new Foo(model, Model.MIDI_MAPPINGS));
		
		this.initComponents(listener, inpPort, inpMappings);
	}
	private void initComponents(final MainPanelListener listener, final JTextField inpPort, final JTextArea inpMappings) {
		inpPort.setToolTipText("Enter a (receivable) MIDI Port Name");
		inpMappings.setToolTipText("Define MIDI Mappings, e.g.: 'l_hand(#torso), X, [0.0 .. 1.0 => 0 .. 127], 0, 1'");
		inpPort.setFont(StyleConstants.FONT);
		inpMappings.setFont(StyleConstants.FONT);
		
		final JScrollPane mappingsScrollable = new JScrollPane(inpMappings);
		inpMappings.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		mappingsScrollable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		final JPanel northPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.gridy = 0;
		northPanel.add(new JLabel("MIDI Port:"), c);
		c.gridy = 1;
		northPanel.add(inpPort, c);
		
		c.gridwidth = 1;
		c.gridy = 2;
		northPanel.add(new JLabel("MIDI Mappings:"), c);
		c.gridx = 1;
		c.weightx = 0.0;
		final JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				listener.onToggleHelp();
		}});
		northPanel.add(btnHelp, c);

		c.weightx = 1.0;
		c.gridx = 0;
		c.gridwidth = 2;
		
		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(mappingsScrollable, BorderLayout.CENTER);
	}
}
