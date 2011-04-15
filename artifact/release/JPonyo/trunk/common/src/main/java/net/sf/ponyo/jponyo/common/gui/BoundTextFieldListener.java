package net.sf.ponyo.jponyo.common.gui;

import javax.swing.text.JTextComponent;

import net.sf.ponyo.jponyo.common.binding.BindingListener;

public class BoundTextFieldListener implements BindingListener {
	private final JTextComponent text;
	
	public BoundTextFieldListener(final JTextComponent text) {
		this.text = text;
	}

	public void onValueChanged(Object newValue) {
		final String newText = (String) newValue;
		if(this.text.getText().equals(newText) == false) {
			this.text.setText(newText);
		}
	}
	
}
