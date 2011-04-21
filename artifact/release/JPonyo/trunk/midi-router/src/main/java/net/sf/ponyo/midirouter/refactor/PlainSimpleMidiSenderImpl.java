package net.sf.ponyo.midirouter.refactor;

import javax.sound.midi.Receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PlainSimpleMidiSenderImpl implements SimpleMidiSender {

	private static final Log LOG = LogFactory.getLog(PlainSimpleMidiSenderImpl.class);

	private static final int ATTACK_VELOCITY = 127;
	
	private Receiver openedReceiver;
	
	/** 0 - 15 */
	private final int noteChannel;
	
	
	public PlainSimpleMidiSenderImpl(final int noteChannel) {
		this.noteChannel = noteChannel;
	}

	public final void doSendMidiNote(final int noteValue) {
		new Thread(new Runnable() {
			@SuppressWarnings("synthetic-access")
			public void run() {
				PlainSimpleMidiSenderImpl.this.sendMidiNote(noteValue);
		}}).start();
	}
	
	private void sendMidiNote(final int noteValue) {
//		LOG.info("sendHitWallMidi(controllerValue=" + noteValue + ")");
//		this.checkOpenedReceiver();
//		
//		final ShortMessage msg = MidiUtil.createMessageByStatus(144/*note channel1 on*/ + this.noteChannel,
//				noteValue, ATTACK_VELOCITY);
//		this.openedReceiver.send(msg, 0);
//		
//		try {
//			Thread.sleep(300);
//		} catch (final InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		final ShortMessage msg2 = MidiUtil.createMessageByStatus(128/*note channel1 off*/ + this.noteChannel,
//				noteValue, ATTACK_VELOCITY);
//		this.openedReceiver.send(msg2, 0);
	}
	
	private void checkOpenedReceiver() {
//		if(this.openedReceiver != null) {
//			return;
//		}
//		final MidiSystem sys = new MidiSystemStaticWrapper(); // MINOR use guice dependency!
//		LOG.debug("fetching MIDI devices ...");
//		final Info[] infos = sys.listMidiDeviceInfos();
//		
//		final String portName = "IAC Driver - Chrisi A";
//		MidiDevice device = null;
//		for (Info info : infos) {
//			final MidiDevice currentDevice = sys.getMidiDevice(info);
//			if(info.getName().equals(portName) &&
//				currentDevice.getMaxReceivers() != 0) {
//				device = currentDevice;
//				break;
//			}
//		}
//		
//		if(device == null) {
//			throw new RuntimeException("Could not find midi port.");
//		}
//		
//		this.openedReceiver = MidiUtil.openReceiver(portName, device);
	}

}
