package net.pulseproject.mkinector.debugapp.scenario1;

import net.pulseproject.josceleton.Josceleton;
import net.pulseproject.mkinector.josceleton.api.JosceletonConnection;
import net.pulseproject.mkinector.josceleton.api.entity.BodyPart;
import net.pulseproject.mkinector.josceleton.api.entity.XyzDirection;
import net.pulseproject.mkinector.josceleton.api.gesture.GestureListener;
import net.pulseproject.mkinector.josceleton.gesture.api.HitWallGesture;
import net.pulseproject.mkinector.josceleton.gesture.api.HitWallGestureResult;
import net.pulseproject.mkinector.josceleton.gesture.internal.HitWallGestureImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;

/** triggered wenn haende (links oder rechts) nahe kommt (z klein ist) */
public class Scenario1 {

	private static final Log LOG = LogFactory.getLog(Scenario1.class);
	
	private final JosceletonConnection connection;
	
	@Inject
	public Scenario1(final JosceletonConnection connection) {
		this.connection = connection;
	}

	public final void registerGestures(final SimpleMidiSender sender) {
		LOG.info("registerGestures(..)");
		System.out.println("HINT: DONT FORGET TO START  M I D I S W I N G  APPLICATION");
		
		this.createHitWallGestureAndRegister(sender, Josceleton.Body().Hand().Left(), true);
		this.createHitWallGestureAndRegister(sender, Josceleton.Body().Hand().Right(), false);
	}
	
	private void createHitWallGestureAndRegister(
			final SimpleMidiSender sender, final BodyPart bodyPart, final boolean isSendingHighTones) {
		final XyzDirection xyzDirection = XyzDirection.Z;
		final double triggerValue = 0.9;
		final boolean checkTriggerForHigher = false;
		
		final HitWallGesture hitWallGesture =
			new HitWallGestureImpl(bodyPart, xyzDirection, triggerValue, checkTriggerForHigher);
		final GestureListener<HitWallGestureResult> listener = new HitWallMidiSender(sender, isSendingHighTones);
		hitWallGesture.addListener(listener);
		
		this.connection.addListener(hitWallGesture);
	}
	
	/*
	public static void mainxx(@SuppressWarnings("unused") final String[] args) throws Exception {
		final MidiSystem sys = new MidiSystemStaticWrapper();
		System.out.println("fetching devices ...");
		final Info[] infos = sys.listMidiDeviceInfos();
		
//		final String portName = "Java Sound Synthesizer";
//		final String portName = "Real Time Sequencer";
		final String portName = "IAC Driver - Chrisi A";
		MidiDevice device = null;
		for (Info info : infos) {
			MidiDevice currentDevice = sys.getMidiDevice(info);
			if(info.getName().equals(portName) &&
				currentDevice.getMaxReceivers() != 0) {
				device = currentDevice;
				break;
			}
		}
		if(device == null) {
			throw new RuntimeException("Could not find midi port.");
		}
		
//		Synthesizer syn = javax.sound.midi.MidiSystem.getSynthesizer();
//		syn.open();
//		Soundbank sb = syn.getDefaultSoundbank();
//		System.out.println("sb: " + sb);
		// http://jsresources.org/faq_midi.html#soft_or_hard_synth
		// If Synthesizer.getDefaultSoundbank() returns null, the software synthesis engine wasn't initialized because
		// no soundbank was available. In this case, the hardware synthesizer (or an external MIDI port) is used.
		// If the return value is not null, you can assume that the software synthesis engine is used. (Matthias)
		
		System.out.println("opening device (max receiver/transmitter: " +
				device.getMaxReceivers() + "/" + device.getMaxTransmitters() + ") ...");
		final Receiver r = MidiUtil.openReceiver(portName, device);
		
		System.out.println("sending MIDI message ...");
		final ShortMessage msg = new ShortMessage();
		// 50 ... D2
		// 40 ... E1
		// 20 ... G#-1
		msg.setMessage(144, 80, 127); // 144 note on channel1
		r.send(msg, 0);
		
		System.out.println("sleeping 2secs ...");
		Thread.sleep(2000);
		
		final ShortMessage msg2 = new ShortMessage();
		msg2.setMessage(128, 50, 127); // 128 note channel1 off
		r.send(msg2, 0);
		
		Thread.sleep(50);
		System.out.println("quit.");
	}
	 */
}
