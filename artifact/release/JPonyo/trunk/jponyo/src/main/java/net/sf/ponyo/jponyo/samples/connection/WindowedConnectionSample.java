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

import net.sf.ponyo.jponyo.connection.ConnectionListener;
import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnector;
import net.sf.ponyo.jponyo.connection.jna.JnaConnection;
import net.sf.ponyo.jponyo.connection.jna.JnaConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WindowedConnectionSample {

	private static final Log LOG = LogFactory.getLog(WindowedConnectionSample.class);
	public static final String CONFIG_PATH = "/ponyo/niconfig.xml";
	public static final String ONI_PATH = "/ponyo/oni.oni";
	
	static int i = 0;
	
	public static void main(String[] args) {
		final JDialog dialogInit = new JDialog();
		dialogInit.setTitle("Loading PonyoNI ...");
		dialogInit.getContentPane().add(new JLabel("Loading ... one moment"));
		dialogInit.pack();
		dialogInit.setVisible(true);
		
		final JTextArea txtOutput = new JTextArea();

//		Connector connector = new OscConnector();
//		Connector connector = new JnaByRecordingConnector(ONI_PATH);
		JnaConnector connector = new JnaByConfigConnector(CONFIG_PATH);
		
		System.out.println("Starting up PonyoNI ...");
//		final Connection connection = connector.openConnection();
		final JnaConnection connection = connector.openConnection();
		System.out.println("Starting up PonyoNI ... DONE");
		
		connection.addListener(new ConnectionListener() {
			public void onUserMessage(int userId, int userState) {
				LOG.debug("onUserMessage(userId=" + userId + ", userState=" + userState + ")");
				txtOutput.setText("USER: ID="+userId+", STATE="+userState+"\n" + txtOutput.getText());
			}
			public void onJointMessage(int userId, int jointId, float x, float y, float z) {
				if(i++ == 100) {
					i = 0;
					LOG.trace("onJointMessage(userId="+userId+", jointId="+jointId+", coord="+x+"/"+y+"/"+z+")");
					txtOutput.setText("JOINT: userId="+userId+", jointId="+jointId+", coord="+x+"/"+y+"/"+z+"\n" + txtOutput.getText());
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
