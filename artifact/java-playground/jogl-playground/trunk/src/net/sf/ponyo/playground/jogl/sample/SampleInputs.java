package net.sf.ponyo.playground.jogl.sample;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class SampleInputs {
	
	private SampleInputs() {
		// empty
	}
	
	public interface InputCallback {
		void onValueSet(Object value);
	}
	
	
	public static abstract class AbstractInput implements SampleInput {
		
		private final String label;
		
		public AbstractInput(String label) {
			this.label = label;
		}

		public final String getLabel() {
			return this.label;
		}
		
	}

	
	public static class BooleanInput extends AbstractInput {
		
		final JCheckBox checkBox;
		Boolean value;
		
		public BooleanInput(String label, boolean initValue) {
			super(label);
			this.checkBox = new JCheckBox();
			this.value = Boolean.valueOf(initValue);
			
			this.checkBox.setSelected(initValue);
			this.checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BooleanInput.this.value = Boolean.valueOf(BooleanInput.this.checkBox.isSelected());
				}
			});
		}
		
		public final Component asComponent() {
			return this.checkBox;
		}
		public Boolean getValue() {
			return this.value;
		}
	}
	
	
	public static class RangeInput extends AbstractInput {
		
		final JSlider slideUserValue = new JSlider();
		Float value;
		
		public RangeInput(String label, float min, float max, float initValue) {
			super(label);
			this.value = Float.valueOf(initValue);
			
			this.slideUserValue.setMinimum(Math.round(min * 100));
			this.slideUserValue.setMaximum(Math.round(max * 100));
			this.slideUserValue.setValue(Math.round(initValue * 100));
			this.slideUserValue.addChangeListener(new ChangeListener() { @Override public void stateChanged(ChangeEvent event) {
				float valueFloat = RangeInput.this.slideUserValue.getValue() / 100.0f;
				RangeInput.this.value = Float.valueOf(valueFloat);
			}});
		}
		
		public final Component asComponent() {
			return this.slideUserValue;
		}
		public Float getValue() {
			return this.value;
		}
		
	}
}
