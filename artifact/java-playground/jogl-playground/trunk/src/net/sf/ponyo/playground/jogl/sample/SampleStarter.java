package net.sf.ponyo.playground.jogl.sample;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.sun.opengl.util.Animator;

public class SampleStarter {
	
	public static void main(String[] args) {
//		start(new Sample1ItCouldNotBeSimpler());
//		start(new Sample2Pyramide());
//		start(new Sample3Something());
//		start(new Sample4Points());
		
		start(new Sample5Enabler());
	}
	
	public static void start(final Sample sample) {
		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setDoubleBuffered(true);
		capabilities.setHardwareAccelerated(true);
		
		GLCanvas canvas = new GLCanvas(capabilities);
		final Animator animator = new Animator(canvas);
		canvas.addGLEventListener(sample);
		
		final JFrame frame = new JFrame();
		frame.setTitle(sample.getClass().getSimpleName());
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
		if(sample.getInputs() != null && sample.getInputs().isEmpty() == false) {
			for (SampleInput input : sample.getInputs()) {
				JPanel inputPanel = new JPanel();
				inputPanel.add(new JLabel(input.getLabel() + ":"));
				inputPanel.add(input.asComponent());
				northPanel.add(inputPanel);
			}
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