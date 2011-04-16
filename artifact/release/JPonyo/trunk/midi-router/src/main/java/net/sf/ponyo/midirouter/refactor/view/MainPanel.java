package net.sf.ponyo.midirouter.refactor.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import net.sf.ponyo.jponyo.common.binding.BindingListener;
import net.sf.ponyo.midirouter.Model;
import net.sf.ponyo.midirouter.refactor.LogUtil;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -4470277649663655733L;
	
	private final JTextArea logField = new JTextArea(6, 85);
	
	private final MainPanelListener listener;

	private final JButton btnStartStop = new JButton();
	private final JButton btnReload = new JButton();
	private final JButton btnViewConsole = new JButton();
	
	
	
	private final Model model;
	public MainPanel(final Model model, final MainPanelListener listener) {
		this.model = model;
		this.listener = listener;
		
		LogUtil.setLogField(this.logField);
		

		this.logField.setFont(StyleConstants.FONT);

		model.addListenerFor(Model.STATE, new BindingListener() {
			public final void onValueChanged(Object newValue) {
				final int state = ((Integer) newValue).intValue();
				
				btnReload.setEnabled(state == Model.STATE_PROCESSING);
				btnViewConsole.setEnabled(state == Model.STATE_PROCESSING);
				btnStartStop.setEnabled(state != Model.STATE_CONNECTING);
				
				if(state == Model.STATE_PROCESSING) {
					btnStartStop.setText("Stop");
					btnStartStop.setToolTipText("Click to close Connection");
				} else if(state == Model.STATE_IDLE){
					btnStartStop.setText("Start");
					btnStartStop.setToolTipText("Click to open Connection");
				}
		}});
		
		this.initComponents();
	}
	
	@SuppressWarnings("synthetic-access")
	private void initComponents() {
		this.btnStartStop.setPreferredSize(new Dimension(130, 40));
		this.btnReload.setPreferredSize(new Dimension(130, 40));
		this.btnViewConsole.setPreferredSize(new Dimension(130, 40));
		this.btnReload.setText("Reload Mappings");
		this.btnViewConsole.setText("View Console");
		
		this.btnStartStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onToggleStartStop();
			}
		});
		this.btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onReloadMidiMappings();
			}
		});
		this.btnViewConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.onToggleConsole();
			}
		});
		
		final JPanel westPanel = new JPanel(new BorderLayout());
		westPanel.add(new ConfigurationPanel(this.model, listener), BorderLayout.CENTER);
		final JPanel cmdPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1.0;
		cmdPanel.add(this.btnStartStop, c);
		
		c.weightx = 0.0;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 1;
		cmdPanel.add(this.btnReload, c);
		c.gridx = 2;
		cmdPanel.add(this.btnViewConsole, c);
		westPanel.add(cmdPanel, BorderLayout.SOUTH);
		
		final JScrollPane logScrolled = new JScrollPane(this.logField);
		logScrolled.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		
		this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, logScrolled);
		this.split.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.split.setResizeWeight(0.0);
		this.split.setDividerLocation(this.model.recentDividerLocation);
		this.setLayout(new BorderLayout());
		this.add(this.split, BorderLayout.CENTER);
	}
	private JSplitPane split;
	public JButton getDefaultButton() {
		return this.btnStartStop;
	}
	
	public void tearDown() {
		this.model.recentDividerLocation = this.split.getDividerLocation();
	}
}
