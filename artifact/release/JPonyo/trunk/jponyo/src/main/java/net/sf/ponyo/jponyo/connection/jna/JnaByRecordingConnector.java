package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.connection.Connector;

/**
 * @since 0.1
 */
public class JnaByRecordingConnector implements Connector {
	
	private final String recordingOniPath;

	/**
	 * @since 0.1
	 */
	public JnaByRecordingConnector(String recordingOniPath) {
		this.recordingOniPath = recordingOniPath;
	}

	/**
	 * @since 0.1
	 */
	public Connection openConnection() {
		return new JnaConnection(this.recordingOniPath).openByOniRecording();
	}
}
