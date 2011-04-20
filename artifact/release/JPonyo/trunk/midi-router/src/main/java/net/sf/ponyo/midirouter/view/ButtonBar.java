package net.sf.ponyo.midirouter.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.common.async.Async;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.midirouter.refactor.ButtonBarListener;

public class ButtonBar extends JPanel implements Async<ButtonBarListener> {

	private static final long serialVersionUID = -4832983778195118152L;
	
	private final DefaultAsync<ButtonBarListener> async = new DefaultAsync<ButtonBarListener>();
	
	public ButtonBar() {
		JButton btnStartStop = new JButton("Start");
		btnStartStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				dispatchStartStopClicked();
			}
		});
		btnStartStop.setToolTipText("Just start that damn kreiwl!");
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				dispatchReloadClicked();
			}
		});
		btnReload.setToolTipText("On-the-fly reload mappings");
		
		this.setLayout(new FlowLayout());
		this.add(btnStartStop);
		this.add(btnReload);
	}

	void dispatchStartStopClicked() {
		for (ButtonBarListener listener : this.async.getListeners()) {
			listener.onStartStopClicked();
		}
	}

	void dispatchReloadClicked() {
		for (ButtonBarListener listener : this.async.getListeners()) {
			listener.onReloadClicked();
		}
	}
	
	public void addListener(ButtonBarListener listener) {
		this.async.addListener(listener);
	}

	public void removeListener(ButtonBarListener listener) {
		this.async.removeListener(listener);
	}

}
