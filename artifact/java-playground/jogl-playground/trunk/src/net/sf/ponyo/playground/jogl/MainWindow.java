package net.sf.ponyo.playground.jogl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jponyo.GlobalData;

import com.sun.opengl.util.Animator;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -7367551475234626321L;
	
	private final GLCanvas canvas = new GLCanvas();
	private final Animator animator = new Animator(this.canvas);
	private final MainWindowGL gl;
	
	public MainWindow(GlobalData data, final MainWindowListener listener) {
		super("Jogl Playground");
		
		this.gl = new MainWindowGL(data);
		this.canvas.addGLEventListener(this.gl);
	    this.initComponents(listener);
	    
	    this.addWindowListener(new WindowAdapter() {
	        @Override public void windowClosing(WindowEvent e) {
	        	System.out.println("windowClosing()");
	        	new Thread(new Runnable() {
					public void run() {
	        			listener.onQuit();
	        		}
	        	}).start();
	        }
	    });
	}
	
	private void initComponents(final MainWindowListener listener) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.canvas, BorderLayout.CENTER);
		
		JPanel cmdPanel = new JPanel();
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			System.out.println("btnQuit clicked");
			listener.onQuit();
		}});
		cmdPanel.add(btnQuit);
		panel.add(cmdPanel, BorderLayout.SOUTH);
		
		this.getRootPane().setDefaultButton(btnQuit);
		this.getContentPane().add(panel);
	}
	
	public void display() {
		this.setVisible(true);
		this.animator.start();
	}

	public void stop() {
		this.animator.stop();
	}
}
