package net.sf.ponyo.midirouter.view;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public abstract class MenuItemX extends JMenuItem {
	
	private static final long serialVersionUID = -177732496785061910L;

	public MenuItemX(String label, ActionListener listener) {
		super(label);
		this.addActionListener(listener);
	}

	private MenuItemX(String label) {
		super(label);
	}
	
	public abstract void callback(MainMenuBarListener listener);
	

	public static class DisabledMenuItemX extends MenuItemX {

		private static final long serialVersionUID = 3467403950250778732L;

		@SuppressWarnings("synthetic-access")
		public DisabledMenuItemX(String label) {
			super(label);
			this.setEnabled(false);
		}

		@Override
		public void callback(MainMenuBarListener listener) {
			// do nothing
		}
		
	}
}
