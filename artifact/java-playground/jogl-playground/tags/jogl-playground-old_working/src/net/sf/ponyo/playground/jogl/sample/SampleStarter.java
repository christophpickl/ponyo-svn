package net.sf.ponyo.playground.jogl.sample;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.opengl.util.Animator;

public class SampleStarter {
	
	public interface Sample extends GLEventListener {
		float[] getUserValueMinMax();
		void setUserValue(float userValue);
	}
	public static abstract class SimpleSample implements Sample {
		@Override  public void setUserValue(float userValue) { /*unused*/ }
		@Override public float[] getUserValueMinMax() { return null; }
	}
	public static void main(String[] args) {
//		start(new Sample1ItCouldNotBeSimpler());
//		start(new Sample2Pyramide());
		start(new Sample3Something());
	}
	
	public static void start(final Sample sample) {
		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setDoubleBuffered(true);
		capabilities.setHardwareAccelerated(true);
		
		GLCanvas canvas = new GLCanvas(capabilities);
		final Animator animator = new Animator(canvas);
		canvas.addGLEventListener(sample);
		
		final JFrame frame = new JFrame();
		frame.setSize(800, 600);
		frame.setLocation(100, 40);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) {
			new Thread(new Runnable() {
				public void run() {
					System.out.println("windowClosing()");
					frame.setVisible(false);
					animator.stop();
					frame.dispose();
					System.exit(0);
				}
			}).start();
		}});
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canvas, BorderLayout.CENTER);

		JPanel northPanel = new JPanel();
		final float[] userValueMinMax = sample.getUserValueMinMax();
		if(userValueMinMax != null) {
			final JSlider slideUserValue = new JSlider();
			northPanel.add(slideUserValue);
			slideUserValue.setMinimum(Math.round(userValueMinMax[0] * 100));
			slideUserValue.setMaximum(Math.round(userValueMinMax[1] * 100));
			slideUserValue.addChangeListener(new ChangeListener() { @Override public void stateChanged(ChangeEvent event) {
				float valueFloat = slideUserValue.getValue() / 100.0f;
				sample.setUserValue(valueFloat);
			}});
		}
		
		panel.add(northPanel, BorderLayout.NORTH);
		
		JButton btnQuit = new JButton("QUIT");
		btnQuit.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent arg0) {
			new Thread(new Runnable() {
				public void run() {
					System.out.println("btnQuit()");
					frame.setVisible(false);
					animator.stop();
					frame.dispose();
					System.exit(0);
				}
			}).start();
		}}); 
		panel.add(btnQuit, BorderLayout.SOUTH);
		frame.getRootPane().setDefaultButton(btnQuit);
		frame.getContentPane().add(panel);
		
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
			System.out.println("displaying window");
			animator.start();
			frame.setVisible(true);
		}});
	}
}