package net.sf.ponyo.midirouter.refactor.ooold;


class HitWallMidiSender { /*write it*/ } // TODO MIDI @HitWallMidiSender reimplement 
//implements GestureListener<HitWallGestureResult> {
//
//	private final SimpleMidiSender midiSender;
//	
//	private final boolean isSendingHighTones;
//	
//	
//	public HitWallMidiSender(final SimpleMidiSender midiSender, final boolean isSendingHighTones) {
//		this.midiSender = midiSender;
//		this.isSendingHighTones = isSendingHighTones;
//	}
//
//
//	@Override
//	public void onGestureDetected(final HitWallGestureResult result) {
//		System.out.println("HitWallMidiSender: " +
//				"FIXMEEEEEEEEEE check values (expected start/end for high/low tunes) in " +
//				"MidiKinectorApp$HitWallMidiSender#onGestureCaptured(..)");
//		
//		final Coordinate coordinate = result.getCoordinate();
//		
//		final int expectedStart = this.isSendingHighTones ? 66 : 0;
//		final int expectedEnd = this.isSendingHighTones ? 127 : 65;
//		
//		final double y = coordinate.y();
//		final int noteValue = MathUtil.relativateTo(70/*somewhere where hips are*/, 10,
//				Util.round3dPoint(y), expectedStart, expectedEnd);
//		
//		this.midiSender.doSendMidiNote(noteValue);
//	}
//}
