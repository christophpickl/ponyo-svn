/**
 * 
 */
package net.sf.ponyo.midirouter.refactor.view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

import net.sf.ponyo.midirouter.Model;

public class Foo extends KeyAdapter {
	private final Model model;
	private final String key;
	
	public Foo(Model model, String key) {
		this.model = model;
		this.key = key;
	}
	@Override public void keyReleased(KeyEvent e) {
		final JTextComponent text = (JTextComponent) e.getSource();
		
		if(this.model.get(this.key).equals(text.getText()) == true) {
			return;
		}
		
		this.model.set(this.key, text.getText());
	}
}