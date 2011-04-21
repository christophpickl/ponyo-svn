package net.sf.ponyo.jponyo.connection.jna;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * @since 0.1
 */
public class JnaByRecordingConnectorImpl extends AbstractJnaConnector {
	
	private static final Log LOG = LogFactory.getLog(JnaByRecordingConnectorImpl.class);
	
	private final String recordingOniPath;

	/**
	 * @since 0.1
	 */
	@Inject
	public JnaByRecordingConnectorImpl(@Assisted String recordingOniPath) {
		LOG.debug("new JnaByRecordingConnector(..)");
		this.recordingOniPath = recordingOniPath;
	}

	/**
	 * @since 0.1
	 */
	public JnaConnection openConnection() {
		return new JnaConnection(this.recordingOniPath).openByOniRecording();
	}
}
