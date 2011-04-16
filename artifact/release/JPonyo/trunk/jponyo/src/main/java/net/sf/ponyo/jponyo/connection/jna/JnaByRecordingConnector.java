package net.sf.ponyo.jponyo.connection.jna;



/**
 * @since 0.1
 */
public class JnaByRecordingConnector extends AbstractJnaConnector {
	
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
	public JnaConnection openConnection() {
		return new JnaConnection(this.recordingOniPath).openByOniRecording();
	}
}
