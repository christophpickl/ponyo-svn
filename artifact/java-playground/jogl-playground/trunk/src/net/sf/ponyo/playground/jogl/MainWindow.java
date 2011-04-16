package net.sf.ponyo.playground.jogl;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.global.GlobalSpace;

import com.sun.opengl.util.Animator;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -7367551475234626321L;
	
	private final Animator animator;
	private final GlobalSpace data;
//	private final SkeletonNumberDialog skeletonDialog = new SkeletonNumberDialog();
	
	public MainWindow(GlobalSpace data, final MainWindowListener listener) {
		super("Jogl Playground");
		this.data = data;
		
		GLCapabilities glCapabilities = new GLCapabilities();
		glCapabilities.setDoubleBuffered(true);
		glCapabilities.setHardwareAccelerated(true);
		
		GLCanvas canvas = new GLCanvas(glCapabilities);
		this.animator = new Animator(canvas);
		
		canvas.addGLEventListener(new MainWindowGL(this.data));
	    this.initComponents(canvas, listener);
	    
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
	@Override public void setVisible(boolean visible) {
		super.setVisible(visible);
		Point p = this.getLocation();
//		this.skeletonDialog.setLocation(p.x + this.getWidth() + 5/*gap*/, p.y);
//		this.skeletonDialog.setVisible(visible);
	}
	
	private void initComponents(GLCanvas canvas, final MainWindowListener listener) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canvas, BorderLayout.CENTER);
		
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

	public void onJointUpdated() {
//		this.skeletonDialog.update(this.data);
	}
}
