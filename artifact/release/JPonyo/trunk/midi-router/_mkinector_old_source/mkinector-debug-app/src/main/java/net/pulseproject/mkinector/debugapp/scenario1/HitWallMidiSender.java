package net.pulseproject.mkinector.debugapp.scenario1;

import net.pulseproject.commons.util.MathUtil;
import net.pulseproject.mkinector.josceleton.api.entity.Coordinate;
import net.pulseproject.mkinector.josceleton.api.gesture.GestureListener;
import net.pulseproject.mkinector.josceleton.gesture.api.HitWallGestureResult;
import something.different.Util;

class HitWallMidiSender implements GestureListener<HitWallGestureResult> {

	private final SimpleMidiSender midiSender;
	
	private final boolean isSendingHighTones;
	
	
	public HitWallMidiSender(final SimpleMidiSender midiSender, final boolean isSendingHighTones) {
		this.midiSender = midiSender;
		this.isSendingHighTones = isSendingHighTones;
	}


	@Override
	public void onGestureDetected(final HitWallGestureResult result) {
		// FIXME dooooit
		System.out.println("HitWallMidiSender: " +
				"FIXMEEEEEEEEEE check values (expected start/end for high/low tunes) in " +
				"MidiKinectorApp$HitWallMidiSender#onGestureCaptured(..)");
		
		final Coordinate coordinate = result.getCoordinate();
		
		final int expectedStart = this.isSendingHighTones ? 66 : 0;
		final int expectedEnd = this.isSendingHighTones ? 127 : 65;
		
		final double y = coordinate.y();
		final int noteValue = MathUtil.relativateTo(70/*somewhere where hips are*/, 10,
				Util.round3dPoint(y), expectedStart, expectedEnd);
		
		this.midiSender.doSendMidiNote(noteValue);
	}
}
