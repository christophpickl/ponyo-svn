package net.sf.ponyo.jponyo.common.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

import net.sf.ponyo.jponyo.common.binding.BindingProvider;

/**
 * Use to addKeyListener to a textfield.
 * 
 * @since 0.1
 */
public class ProviderKeyListener<P extends BindingProvider> extends KeyAdapter {
	private final P provider;
	private final String key;
	
	public ProviderKeyListener(P model, String key) {
		this.provider = model;
		this.key = key;
	}
	
	@Override public void keyReleased(KeyEvent e) {
		JTextComponent textSource = (JTextComponent) e.getSource();
		String newEnteredText = textSource.getText();
		
		if(newEnteredText.equals(this.provider.get(this.key)) == true) {
			return;
		}
		
		this.provider.set(this.key, newEnteredText);
	}
}
