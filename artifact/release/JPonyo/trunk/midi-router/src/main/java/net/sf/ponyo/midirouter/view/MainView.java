package net.sf.ponyo.midirouter.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.sf.ponyo.midirouter.view.framework.AbstractMainView;
import net.sf.ponyo.midirouter.view.framework.AbstractMainViewListener;

public class MainView extends AbstractMainView<AbstractMainViewListener> {
	
	private static final long serialVersionUID = 7350458392797569309L;
	
	public MainView() {
		super("Ponyo MIDI Router");
	}
	
	@Override
	protected final Component initComponent() {
		DataFlowPanel dataPanel = new DataFlowPanel();
		Component westPanel = this.createWestPanel();
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel , dataPanel);
		split.setDividerLocation(300);
		split.setResizeWeight(0.0);
		return split;
	}
	
	private Component createWestPanel() {
		ConfigurationPanel configPanel = new ConfigurationPanel();
		ButtonBar buttonBar = new ButtonBar();
		
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
	
}
