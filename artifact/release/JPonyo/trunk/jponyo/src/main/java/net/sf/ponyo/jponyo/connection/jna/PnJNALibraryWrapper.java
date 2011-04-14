package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnJointPositionChangedCallback;
import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnUserStateChangedCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jna.Native;

/**
 * @since 0.1
 */
class PnJNALibraryWrapper {

	/*
	JnaGate gate = new JnaGate();
	try {
		LOG.info("Starting JNA");
//		gate.startWithXml("/ponyo/niconfig.xml");
		gate.startRecording("/ponyo/oni.oni");
	} finally {
		LOG.info("Destroying JNA");
		gate.destroy();
	}
	*/
	private static final Log LOG = LogFactory.getLog(PnJNALibraryWrapper.class);
	
	/**
	 * Lazy initialized to safe valueable user time ;)
	 */
	private static PnJNALibray nativeLibrary;
	
	private final OnUserStateChangedCallback userCallback;
	
	private final OnJointPositionChangedCallback jointCallback;
		
	
	public PnJNALibraryWrapper(OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback) {
		this.userCallback = userCallback;
		this.jointCallback = jointCallback;
	}

	public void startByXmlConfig(String configPath) {
		LOG.debug("startByXmlConfig(configPath=" + configPath + ")");
		this.internalStart(configPath, true);
	}

	public void startByOniRecording(String oniPath) {
		LOG.debug("startByOniRecording(oniPath=" + oniPath + ")");
		this.internalStart(oniPath, false);
	}
	
	private void internalStart(String configOrOniPath, boolean isStartingByConfig) {
		this.initNativeLibrary();
		
		final int resultCode;
		if(isStartingByConfig == true) {
			resultCode = PnJNALibraryWrapper.nativeLibrary.pnStartByXmlConfig(configOrOniPath, this.userCallback, this.jointCallback);
		} else {
			resultCode = PnJNALibraryWrapper.nativeLibrary.pnStartByOniRecording(configOrOniPath, this.userCallback, this.jointCallback);
		}
		LOG.debug("JNA returned resultCode: " + resultCode);
		if(resultCode != 0) {
			throw new RuntimeException("resultCode: " + resultCode);
		}
	}
	
	/**
	 * Postpone library initialisation as late as possible.
	 */
	private void initNativeLibrary() {
		if(PnJNALibraryWrapper.nativeLibrary == null) {
			LOG.debug("Loading native library: " + PnJNALibray.LIB_NAME);
//			System.setProperty("jna.encoding", "");
			PnJNALibraryWrapper.nativeLibrary = (PnJNALibray) Native.loadLibrary(PnJNALibray.LIB_NAME, PnJNALibray.class);
		}
	}

	public void shutdown() {
		LOG.debug("shutdown()");
		PnJNALibraryWrapper.nativeLibrary.pnShutdown();
	}

	
}
