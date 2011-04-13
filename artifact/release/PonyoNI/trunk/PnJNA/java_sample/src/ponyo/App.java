package ponyo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public class App {

	public static void main(String[] args) {
		final PnJNALibray nativeLibrary = (PnJNALibray) Native.loadLibrary(PnJNALibray.LIB_NAME, PnJNALibray.class);
		final JTextArea txtOutput = new JTextArea();
		
		final OnUserStateChangedCallback userCallback = new OnUserStateChangedCallback() {
			@Override public void onUserStateChanged(int userId, int userState) {
				txtOutput.setText("USER: ID="+userId+", STATE="+userState+"\n" + txtOutput.getText());
			}
		};
		final OnJointPositionChangedCallback jointCallback = new OnJointPositionChangedCallback() {
			@Override public void onJointPositionChanged(int userId, int joint, float x, float y, float z) {
				txtOutput.setText("JOINT: ID="+userId+", JOINT="+joint+"\n" + txtOutput.getText());
			}
		};

		JButton btnQuit = new JButton("Quit");
		btnQuit.setEnabled(false);
		final JFrame frame = createWindow(txtOutput, btnQuit);
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
			frame.setVisible(true);
		}});
		
		System.out.println("Starting up PonyoNI ...");
//		nativeLibrary.pnStartRecording("/myopenni/myoni.oni", userCallback, jointCallback);
		
		IntByReference resultCodeRef = new IntByReference();
		nativeLibrary.pnStartWithXml(resultCodeRef, "/myopenni/simple_config.xml", userCallback, jointCallback);
		final int resultCode = resultCodeRef.getValue();
		
		System.out.println("Starting up PonyoNI ... DONE; result code: " + resultCode);
		
		btnQuit.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			System.out.println("pnDestroy() START");
			nativeLibrary.pnDestroy();
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

	interface PnJNALibray extends Library {
		String LIB_NAME = "PnJNA";
		void pnStartWithXml(IntByReference resultCode, String configPath, OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback);
		void pnStartRecording(IntByReference resultCode, String oniPath, OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback);
		void pnDestroy();
	}

	interface OnUserStateChangedCallback extends Callback {
		void onUserStateChanged(int userId, int userState);
	}

	interface OnJointPositionChangedCallback extends Callback {
		void onJointPositionChanged(int userId, int joint, float x, float y, float z);
	}
}
