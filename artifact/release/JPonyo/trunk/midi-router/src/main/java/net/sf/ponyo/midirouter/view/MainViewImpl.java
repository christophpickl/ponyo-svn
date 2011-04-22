package net.sf.ponyo.midirouter.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.jponyo.common.gui.AbstractMainView;
import net.sf.ponyo.midirouter.MidiRouterApp;
import net.sf.ponyo.midirouter.logic.Model;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;
import net.sf.ponyo.midirouter.refactor.ButtonBarListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;

class MainViewImpl
	extends AbstractMainView<MainViewListener, Model>
		implements ButtonBarListener, MainMenuBarListener, MainView {
	
	private static final Log LOG = LogFactory.getLog(MainViewImpl.class);
	private static final long serialVersionUID = 7350458392797569309L;
	private static final int FRAME_INNER_GAP = 8;
	
	private final ConfigurationPanel configPanel;
	private final ButtonBar buttonBar;
	
	@Inject
	public MainViewImpl(Model model, ConfigurationPanel configPanel, ButtonBar buttonBar) {
		super(model, "Ponyo MIDI Router");
		this.configPanel = configPanel;
		this.buttonBar = buttonBar;
        this.setIconImage(MidiRouterApp.IMAGE_FACTORY.getImage("frame_title_logo.png").getImage());
	}
	
	@Override
	protected final Component initComponent(Model model) {
		MainMenuBar menuBar = new MainMenuBar(model);
		menuBar.addListener(this);
		this.setJMenuBar(menuBar);
		
		
		final JTable dataTable = this.initMidiMappingTable(model);
		
		JScrollPane tableScroll = new JScrollPane(dataTable);
		tableScroll.setWheelScrollingEnabled(true);
		JPanel tableWrapper = new JPanel(new BorderLayout());
		tableWrapper.setOpaque(false);
		tableWrapper.add(tableScroll, BorderLayout.CENTER);
		tableWrapper.setBorder(BorderFactory.createEmptyBorder(FRAME_INNER_GAP, 0, FRAME_INNER_GAP, FRAME_INNER_GAP));
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				this.createWestPanel(model), tableWrapper);
		split.setDividerLocation(400);
		split.setResizeWeight(0.0);
		split.setOpaque(false);
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
		this.getRootPane().setDefaultButton(this.buttonBar.getDefaultButton());
		buttonBar.addListener(this);
		
		final JPanel westPanel = new JPanel();
		westPanel.setOpaque(false);
		westPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5, FRAME_INNER_GAP, 0, 0);
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
		
		c.insets = new Insets(0, FRAME_INNER_GAP, 0, 0);
		return westPanel;
	}

	public void onStartStopClicked() {
		for (MainViewListener listener : this.getListeners()) {
			listener.onStartStopClicked();
		}
	}

	public void onReloadClicked() {
		for (MainViewListener listener : this.getListeners()) {
			listener.onReloadClicked();
		}
	}

	public void onMenuQuit() {
		LOG.debug("onMenuQuit()");
		for (MainViewListener listener : this.getListeners()) {
			listener.onQuit();
		}
	}

	public void onMenuMidiPorts() {
		LOG.debug("onMenuMidiPorts()");
		for (MainViewListener listener : this.getListeners()) {
			listener.onToggleMidiPortsWindow();
		}
	}

	public void onMenuHelp() {
		LOG.debug("onMenuHelp()");
		for (MainViewListener listener : this.getListeners()) {
			listener.onToggleHelpWindow();
		}
	}

	public void onMenuAdminConsole() {
		LOG.debug("onMenuHelp()");
		for (MainViewListener listener : this.getListeners()) {
			listener.onToggleAdminConsole();
		}
	}

	public JFrame asJFrame() {
		return this; // just a type reducer
	}
	
}
