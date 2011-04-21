package net.sf.ponyo.jponyo.core;

public interface ContextStarter {

	// Context startProgrammaticConfig(SomeConfigType config);
	Context startXmlConfig(String configXmlPath);

	Context startOniRecording(String recordingOniPath);

	Context startOscReceiverOnPort(int port);
	Context startOscReceiver();

}