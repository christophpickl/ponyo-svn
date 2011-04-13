package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnJointPositionChangedCallback;
import net.sf.ponyo.jponyo.connection.jna.PnJNALibray.OnUserStateChangedCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jna.Native;

/**
 * @since 0.1
 */
class JnaGate implements OnUserStateChangedCallback, OnJointPositionChangedCallback {
	
	private static final Log LOG = LogFactory.getLog(JnaGate.class);
	
	/**
	 * Lazy initialized to safe valueable user time ;)
	 */
	private static PnJNALibray nativeLibrary;

	public JnaGate() {
		if(JnaGate.nativeLibrary == null) {
			LOG.debug("Loading native library: " + PnJNALibray.LIB_NAME);
//			System.setProperty("jna.encoding", "");
			JnaGate.nativeLibrary = (PnJNALibray) Native.loadLibrary(PnJNALibray.LIB_NAME, PnJNALibray.class);
		}
	}

	public void startWithXml(String configPath) {
		int resultCode = JnaGate.nativeLibrary.pnStartWithXml(configPath, this, this);
		LOG.debug("JNA returned resultCode: " + resultCode);
	}

	public void startRecording(String oniPath) {
		int resultCode = JnaGate.nativeLibrary.pnStartRecording(oniPath, this, this);
		LOG.debug("JNA returned resultCode: " + resultCode);
	}

	public void destroy() {
		LOG.debug("destroy()");
		JnaGate.nativeLibrary.pnDestroy();
	}

	public void onUserStateChanged(int userId, int userState) {
		LOG.trace("onUserStateChanged(userId=" + userId + ", userState=" + userState + ")");
	}

	public void onJointPositionChanged(int userId, int joint, float x, float y, float z) {
		System.out.println("joint");
	}
	
}
