package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnJointPositionChangedCallback;
import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnUserStateChangedCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jna.Native;

/**
 * @since 0.1
 */
class JnaGate {

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
	private static final Log LOG = LogFactory.getLog(JnaGate.class);
	
	/**
	 * Lazy initialized to safe valueable user time ;)
	 */
	private static PnJNALibray nativeLibrary;
	
	private final OnUserStateChangedCallback userCallback;
	
	private final OnJointPositionChangedCallback jointCallback;
	
	public JnaGate(OnUserStateChangedCallback userCallback, OnJointPositionChangedCallback jointCallback) {
		this.userCallback = userCallback;
		this.jointCallback = jointCallback;
	}
	
	/**
	 * Postpone library initialisation as late as possible.
	 */
	private void initNativeLibrary() {
		if(JnaGate.nativeLibrary == null) {
			LOG.debug("Loading native library: " + PnJNALibray.LIB_NAME);
//			System.setProperty("jna.encoding", "");
			JnaGate.nativeLibrary = (PnJNALibray) Native.loadLibrary(PnJNALibray.LIB_NAME, PnJNALibray.class);
		}
	}

	public void startWithXml(String configPath) {
		this.initNativeLibrary();
		
		LOG.debug("startWithXml(configPath=" + configPath + ")");
		int resultCode = JnaGate.nativeLibrary.pnStartWithXml(configPath, this.userCallback, this.jointCallback);
		LOG.debug("JNA returned resultCode: " + resultCode);
		if(resultCode != 0) {
			throw new RuntimeException("resultCode: " + resultCode);
		}
	}

	public void startRecording(String oniPath) {
		this.initNativeLibrary();
		
		LOG.debug("startRecording(oniPath=" + oniPath + ")");
		int resultCode = JnaGate.nativeLibrary.pnStartRecording(oniPath, this.userCallback, this.jointCallback);
		LOG.debug("JNA returned resultCode: " + resultCode);
		if(resultCode != 0) {
			throw new RuntimeException("resultCode: " + resultCode);
		}
	}

	public void destroy() {
		LOG.debug("destroy()");
		JnaGate.nativeLibrary.pnDestroy();
	}

	
}
