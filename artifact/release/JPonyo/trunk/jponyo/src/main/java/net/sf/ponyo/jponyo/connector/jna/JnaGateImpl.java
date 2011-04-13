package net.sf.ponyo.jponyo.connector.jna;

import net.sf.ponyo.jponyo.connector.jna.PnJNALibray.OnJointPositionChangedCallback;
import net.sf.ponyo.jponyo.connector.jna.PnJNALibray.OnUserStateChangedCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jna.Native;

public class JnaGateImpl implements OnUserStateChangedCallback, OnJointPositionChangedCallback {
	
	private static final Log LOG = LogFactory.getLog(JnaGateImpl.class);
	
	/**
	 * Lazy initialized to safe valueable user time ;)
	 */
	private static PnJNALibray nativeLibrary;

	public JnaGateImpl() {
		if(JnaGateImpl.nativeLibrary == null) {
			LOG.debug("Loading native library: " + PnJNALibray.LIB_NAME);
//			System.setProperty("jna.encoding", "");
			JnaGateImpl.nativeLibrary = (PnJNALibray) Native.loadLibrary(PnJNALibray.LIB_NAME, PnJNALibray.class);
		}
	}

	public void startWithXml(String configPath) {
		int resultCode = JnaGateImpl.nativeLibrary.pnStartWithXml(configPath, this, this);
		LOG.debug("JNA returned resultCode: " + resultCode);
	}

	public void startRecording(String oniPath) {
		int resultCode = JnaGateImpl.nativeLibrary.pnStartRecording(oniPath, this, this);
		LOG.debug("JNA returned resultCode: " + resultCode);
	}

	public void destroy() {
		LOG.debug("destroy()");
		JnaGateImpl.nativeLibrary.pnDestroy();
	}

	public void onUserStateChanged(int userId, int userState) {
		LOG.trace("onUserStateChanged(userId=" + userId + ", userState=" + userState + ")");
	}

	public void onJointPositionChanged(int userId, int joint, float x, float y, float z) {
		System.out.println("joint");
	}
	
}
