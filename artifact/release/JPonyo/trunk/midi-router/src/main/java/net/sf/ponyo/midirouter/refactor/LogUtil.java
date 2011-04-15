package net.sf.ponyo.midirouter.refactor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public class LogUtil {
	
	public static final int LOG_JOINT_EVERY = 10;
	
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss.S");

	private static final int MAX_CHARS = 6000; // 1 page approx 800
	private static final int CHARS_CUT_OFF_TO = 5000;
	
	private static JTextArea logField;
	
	private static int logCount = 0;

	public static void log(String msg) {
		if(logField == null) {
			return;
		}
		
		String date = FORMAT.format(new Date());
		if(date.length() == 11) {
			date = date.substring(0, date.length() - 2) + "0" + date.substring(date.length() - 2); // 12:18:09.91 ==> 12:18:09.091
		} else if(date.length() == 10) {
			date = date.substring(0, date.length() - 1) + "00" + date.substring(date.length() - 1); // 12:18:09.4 ==> 12:18:09.004
		}
		
		final String newMsg = "[" + date +"] " + msg ;
		String oldText;
		final String origText = logField.getText();
		if(origText.length() < MAX_CHARS) {
			oldText = origText;
		} else {
			oldText = origText.substring(0, CHARS_CUT_OFF_TO);
		}
		logField.setText(newMsg + "\n" + oldText);
		
		logCount++;
	}

	public static void setLogField(JTextArea field) {
		logField = field;
	}
	

	public static void clearLog() {
		if(logField != null) {
			logField.setText("");
		}
	}
}
