package net.sf.ponyo.midirouter.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import net.sf.ponyo.jponyo.common.async.Async;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.midirouter.logic.Model;
import net.sf.ponyo.midirouter.view.MenuItemX.DisabledMenuItemX;
import net.sourceforge.jpotpourri.tools.PtUserSniffer;

public class MainMenuBar extends JMenuBar implements ActionListener, Async<MainMenuBarListener> {

	private static final long serialVersionUID = 5922405462789754486L;
	
	private final DefaultAsync<MainMenuBarListener> async = new DefaultAsync<MainMenuBarListener>();
	
	public MainMenuBar(Model model) {
		JMenu menuApp = new JMenu("File");
		MenuItemX menuAppItemVersion = new DisabledMenuItemX("Version: " + model.appVersion);
		
		if(PtUserSniffer.isMacOSX() == false) {
			MenuItemX menuAppItemExit = new MenuItemX("Exit", this) {
				private static final long serialVersionUID = 1L;
				@Override public void callback(MainMenuBarListener listener) {
					listener.onMenuQuit();
			}};
			menuAppItemExit.setMnemonic(KeyEvent.VK_X);
			menuAppItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
			menuApp.add(menuAppItemExit);
		}
		
		menuApp.add(menuAppItemVersion);
		this.add(menuApp);

		MenuItemX menuViewItemAdminConsole = new MenuItemX("Admin Console", this) {
			private static final long serialVersionUID = 1L;
			@Override public void callback(MainMenuBarListener listener) {
				listener.onMenuAdminConsole();
		}};
		menuViewItemAdminConsole.setMnemonic(KeyEvent.VK_A);
		menuViewItemAdminConsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.SHIFT_MASK | ActionEvent.META_MASK));

		MenuItemX menuViewItemMidiPorts = new MenuItemX("MIDI Ports", this) {
			private static final long serialVersionUID = 1L;
			@Override public void callback(MainMenuBarListener listener) {
				listener.onMenuMidiPorts();
		}};
		menuViewItemMidiPorts.setMnemonic(KeyEvent.VK_M);
		menuViewItemMidiPorts.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.SHIFT_MASK | ActionEvent.META_MASK));
		
		MenuItemX menuViewItemHelp = new MenuItemX("Help", this) {
			private static final long serialVersionUID = 1L;
			@Override public void callback(MainMenuBarListener listener) {
				listener.onMenuHelp();
		}};
		menuViewItemHelp.setMnemonic(KeyEvent.VK_H);
		menuViewItemHelp.setAccelerator(KeyStroke.getKeyStroke("F1"));

		JMenu menuView = new JMenu("View");
		menuView.add(menuViewItemAdminConsole);
		menuView.add(menuViewItemMidiPorts);
		menuView.addSeparator();
		menuView.add(menuViewItemHelp);
		this.add(menuView);
		
	}

	public void actionPerformed(ActionEvent e) {
		MenuItemX item = (MenuItemX) e.getSource();
		for (MainMenuBarListener listener : this.async.getListeners()) {
			item.callback(listener);
		}
	}

	public void addListener(MainMenuBarListener listener) {
		this.async.addListener(listener);
	}

	public void removeListener(MainMenuBarListener listener) {
		this.async.removeListener(listener);
	}
	
}
