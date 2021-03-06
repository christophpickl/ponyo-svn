package net.sf.ponyo.midirouter.refactor;

import java.util.Collection;

public class SomeUtil {

	public static void handleException(Exception e) {
//		e.printStackTrace();
//		JOptionPane.showMessageDialog(null, e.getMessage(), "Josceleton Midi Route Error", JOptionPane.ERROR_MESSAGE);
//		LogUtil.log("Stack trace:\n" + StringUtil.exceptionToString(e));
//		LogUtil.log("Error: " + e.getMessage());
	}

	public static String fillString(String in, int len) {
		if(in.length() >= len) {
			return in;
		}
		return in + "        ".substring(0, (len - in.length()));
	}
	
	public static String toString(final Collection<MidiMapping> maps) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("JointMidiMappings [\n");
		int i = 1;
		for (final MidiMapping map : maps) {
			if(i != 1) sb.append("\n");
			sb.append("  ").append(i).append(". ").append(map);
			i++;
		}
		sb.append("\n]");
		
		return sb.toString();
	}
}
