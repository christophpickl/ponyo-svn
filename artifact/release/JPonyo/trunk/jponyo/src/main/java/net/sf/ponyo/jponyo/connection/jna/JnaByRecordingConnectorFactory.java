package net.sf.ponyo.jponyo.connection.jna;


public interface JnaByRecordingConnectorFactory {
	
	JnaByRecordingConnectorImpl create(String recordingOniPath);
	
}
