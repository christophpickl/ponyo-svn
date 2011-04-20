package net.sf.ponyo.midirouter.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.sf.ponyo.midirouter.logic.Model;
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
		DataFlowPanel dataPanel = new DataFlowPanel();
		Component westPanel = this.createWestPanel(model);
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel , dataPanel);
		split.setDividerLocation(400);
		split.setResizeWeight(0.0);
		return split;
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
