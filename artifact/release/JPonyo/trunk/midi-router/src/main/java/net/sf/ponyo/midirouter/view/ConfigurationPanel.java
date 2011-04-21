package net.sf.ponyo.midirouter.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.jponyo.common.gui.BoundTextFieldListener;
import net.sf.ponyo.jponyo.common.gui.ProviderKeyListener;
import net.sf.ponyo.midirouter.logic.Model;
import net.sourceforge.jpotpourri.jpotface.inputfield.PtTextFieldSuggester;

import com.google.inject.Inject;

public class ConfigurationPanel extends JPanel {

	private static final long serialVersionUID = -1166748256589496496L;
	
	@Inject
	ConfigurationPanel(Model model) {
		this.setOpaque(false);
		
		final JTextArea inpMappings = new JTextArea();//14, 45);
		model.addListenerFor(Model.MIDI_MAPPINGS, new BoundTextFieldListener(inpMappings));
		inpMappings.addKeyListener(new ProviderKeyListener<Model>(model, Model.MIDI_MAPPINGS));
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		final JTextField inpPort = createMidiPortTextField(model, Model.MIDI_PORT, "Enter a (receivable) MIDI Port Name");
		
		inpMappings.setToolTipText("Define MIDI Mappings, e.g.: 'l_hand(#torso), X, [0.0 .. 1.0 => 0 .. 127], 0, 1'");
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
		JComponent lblMidiPort = new JLabel("MIDI Port:");
		lblMidiPort.setOpaque(false);
		this.add(lblMidiPort, c);
		
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
	
	private JTextField createMidiPortTextField(final Model model, String key, String tooltip) {
		final PtTextFieldSuggester text = new PtTextFieldSuggester(new ArrayList<String>());
		
		model.addListenerFor(Model.MIDI_DEVICES, new BindingListener() {
			public void onValueChanged(Object newValue) {
				List<MidiDevice> devices = model.getMidiDevices();
				List<String> deviceNames = new LinkedList<String>();
				for (MidiDevice midiDevice : devices) {
					if(midiDevice.getMaxReceivers() != 0) {
						deviceNames.add(midiDevice.getDeviceInfo().getName());
					}
				}
				text.setValues(deviceNames);
			}
		});
		
		model.addListenerFor(key, new BoundTextFieldListener(text));
		text.addKeyListener(new ProviderKeyListener<Model>(model, key));
		text.setToolTipText(tooltip);
		text.setFont(Styles.FONT_MONOSPACED);
		return text;
	}
	
}
