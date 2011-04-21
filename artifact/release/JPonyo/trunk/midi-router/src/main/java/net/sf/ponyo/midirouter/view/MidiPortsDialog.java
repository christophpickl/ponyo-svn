package net.sf.ponyo.midirouter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.pulseproject.commons.util.GuiUtil;
import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.midirouter.logic.Model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiPortsDialog extends JDialog {

	private static final long serialVersionUID = 3921063116921815056L;

	private static final Log LOG = LogFactory.getLog(MidiPortsDialog.class);
	
	private final JPanel transmittersPanel = new JPanel();
	private final JPanel receiversPanel = new JPanel();
	
	private final Font headerFont = new Font(Font.DIALOG, Font.BOLD, 12);
	
	public MidiPortsDialog(Model model) {
		this.setTitle("MIDI Ports");

		this.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
		this.setResizable(false);
		this.getContentPane().add(this.initComponents(model));
		this.pack();
		GuiUtil.setCenterLocation(this);
	}
	
	void reinitDevices(List<MidiDevice> devices) {
		this.transmittersPanel.removeAll();
		this.receiversPanel.removeAll();
		
		if(devices == null || devices.isEmpty()) {
			return;
		}

		final JLabel lblReceiver = new JLabel("Receivers:");
		lblReceiver.setFont(this.headerFont);
		this.receiversPanel.add(lblReceiver);
		final JLabel lblTransmitter = new JLabel("Transmitters:");
		lblTransmitter.setFont(this.headerFont);
		this.transmittersPanel.add(lblTransmitter);
		
		for (MidiDevice midiDevice : devices) {
			if(midiDevice.getMaxReceivers() != 0) {
				this.receiversPanel.add(new JLabel(midiDevice.getDeviceInfo().getName()));
			}
			if(midiDevice.getMaxTransmitters() != 0) {
				this.transmittersPanel.add(new JLabel(midiDevice.getDeviceInfo().getName()));
			}
		}
	}

	private Component initComponents(final Model model) {
		model.addListenerFor(Model.MIDI_DEVICES, new BindingListener() {
			public void onValueChanged(Object newValue) {
				reinitDevices(model.getMidiDevices());
		}});
		
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(30, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		
		panel.add(new JLabel("This is a list of all available MIDI ports:"), BorderLayout.NORTH);
		
		this.receiversPanel.setLayout(new BoxLayout(this.receiversPanel, BoxLayout.Y_AXIS));
		panel.add(this.receiversPanel, BorderLayout.WEST);
		
		this.transmittersPanel.setLayout(new BoxLayout(this.transmittersPanel, BoxLayout.Y_AXIS));
		panel.add(this.transmittersPanel, BorderLayout.EAST);
		
		return panel;
	}

}
