package net.sf.ponyo.jponyo.samples.connection;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.Connector;
import net.sf.ponyo.jponyo.connection.JointMessage;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnector;
import net.sf.ponyo.jponyo.connection.jna.JnaByRecordingConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;
import net.sf.ponyo.jponyo.core.DevelopmentConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WindowedConnectionSample {

	private static final Log LOG = LogFactory.getLog(WindowedConnectionSample.class);
	
	static int i = 0;

	// force imports
	@SuppressWarnings("unused") private OscConnector c1;
	@SuppressWarnings("unused") private JnaByRecordingConnector c2;
	@SuppressWarnings("unused") private JnaByConfigConnector c3;
	
	public static void main(String[] args) {
		final JDialog dialogInit = new JDialog();
		dialogInit.setTitle("Loading PonyoNI ...");
		dialogInit.getContentPane().add(new JLabel("Loading ... one moment"));
		dialogInit.pack();
		dialogInit.setVisible(true);
		
		final JTextArea txtOutput = new JTextArea();

		@SuppressWarnings("unused") String oniPath = DevelopmentConstants.ONI_PATH;
		
//		Connector<? extends Connection> connector = new OscConnector();
		Connector<? extends Connection> connector = new JnaByRecordingConnector(DevelopmentConstants.ONI_PATH);
//		Connector<? extends Connection> connector = new JnaByConfigConnector(Constants.XML_PATH);
		
		System.out.println("Starting up PonyoNI ...");
//		final Connection connection = connector.openConnection();
		final Connection connection = connector.openConnection();
		System.out.println("Starting up PonyoNI ... DONE");
		
		connection.addListener(new ConnectionListener() {
			public void onUserMessage(int userId, int userState) {
				LOG.debug("onUserMessage(userId=" + userId + ", userState=" + userState + ")");
				txtOutput.setText("USER: ID="+userId+", STATE="+userState+"\n" + txtOutput.getText());
			}
			public void onJointMessage(JointMessage message) {
				if(i++ == 100) {
					i = 0;
					LOG.trace("onJointMessage(message="+message+")");
					txtOutput.setText("JOINT: "+message+"\n" + txtOutput.getText());
				}
			}
		});
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setEnabled(false);
		final JFrame frame = createWindow(txtOutput, btnQuit);
		SwingUtilities.invokeLater(new Runnable() { public void run() {
			dialogInit.setVisible(false);
			dialogInit.dispose();
			frame.setVisible(true);
		}});
		
		btnQuit.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
			System.out.println("pnDestroy() START");
			connection.close();
			System.out.println("pnDestroy() END");
			frame.setVisible(false);
			frame.dispose(); 
		}});
		btnQuit.setEnabled(true);
	}

	private static JFrame createWindow(JComponent txtOutput, JButton btnQuit) {
		final JFrame frame = new JFrame("PnJNA - Java Sample");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(txtOutput), BorderLayout.CENTER);
		panel.add(btnQuit, BorderLayout.SOUTH);
		frame.getContentPane().add(panel);
		frame.setSize(600, 300);
		frame.setLocation(100, 60);
		return frame;
	}
}
