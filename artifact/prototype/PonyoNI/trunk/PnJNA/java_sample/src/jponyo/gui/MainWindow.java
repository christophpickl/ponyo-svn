package jponyo.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import jponyo.GlobalData;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 6925062641532803991L;

	private final SkeletonNumberPanel drawer = new SkeletonNumberPanel();
	private JButton btnStart = new JButton("Start");
	
	public MainWindow(final MainWindowListener listener) {
		super("JPonyo C++");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent event) {
				listener.onQuit();
			}
		});
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("App for PnJNA!!!"), BorderLayout.NORTH);
		
		JPanel cmdPanel = new JPanel();
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				listener.onQuit();
			}
		});
		
		this.btnStart.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				listener.onStart();
			}
		});
		
		cmdPanel.add(this.btnStart);
		cmdPanel.add(btnQuit);
		panel.add(cmdPanel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(btnQuit);
		
		panel.add(this.drawer, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.getContentPane().add(panel);
		this.pack();
		this.setLocation(50, 50);
	}

	public void update(GlobalData data) {
		this.drawer.update(data);
	}
	
	public interface MainWindowListener {
		void onQuit();

		void onStart();
	}
	
	public void setJnaRunning(boolean jnaRunning) {
		this.btnStart.setEnabled(jnaRunning == false);
	}
}
