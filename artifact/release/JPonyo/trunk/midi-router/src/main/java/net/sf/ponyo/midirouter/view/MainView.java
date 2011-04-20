package net.sf.ponyo.midirouter.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.midirouter.logic.Model;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;
import net.sf.ponyo.midirouter.refactor.ButtonBarListener;
import net.sf.ponyo.midirouter.view.framework.AbstractMainView;

public class MainView
	extends AbstractMainView<MainViewListener, Model>
		implements ButtonBarListener {
	
	private static final long serialVersionUID = 7350458392797569309L;
	
	public MainView(Model model) {
		super(model, "Ponyo MIDI Router");
	}
	
	@Override
	protected final Component initComponent(Model model) {
		final JTable dataTable = this.initMidiMappingTable(model);
		Component westPanel = this.createWestPanel(model);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel , new JScrollPane(dataTable));
		split.setDividerLocation(400);
		split.setResizeWeight(0.0);
		return split;
	}
	
	private JTable initMidiMappingTable(Model model) {
		final MidiMappingTableModel tableModel = new MidiMappingTableModel();
		final MidiMappingTable dataTable = new MidiMappingTable(tableModel);

		model.addListenerFor(Model.ACTIVE_MAPPINGS, new BindingListener() {
			public void onValueChanged(Object newValue) {
				if(newValue == null) {
					tableModel.setMappings(null);
				} else {
					MidiMappings mappings = (MidiMappings) newValue;
					tableModel.setMappings(mappings.getMappings());
				}
			}
		});

		model.addListenerFor(Model.FRAME_COUNT, new BindingListener() {
			public void onValueChanged(Object newValue) {
				tableModel.fireTableDataChanged();
			}
		});
		return dataTable;
	}
	
	private Component createWestPanel(Model model) {
		ConfigurationPanel configPanel = new ConfigurationPanel(model);
		ButtonBar buttonBar = new ButtonBar(model);
		buttonBar.addListener(this);
		
		JPanel westPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		westPanel.add(configPanel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy++;
		c.weighty = 0.0;
		westPanel.add(buttonBar, c);
		
		return westPanel;
	}

	public void onStartStopClicked() {
		for (ButtonBarListener listener : this.getListeners()) {
			listener.onStartStopClicked();
		}
	}

	public void onReloadClicked() {
		for (ButtonBarListener listener : this.getListeners()) {
			listener.onReloadClicked();
		}
	}
	
}
