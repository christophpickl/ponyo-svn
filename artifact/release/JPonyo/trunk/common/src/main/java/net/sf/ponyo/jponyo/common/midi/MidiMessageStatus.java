package net.sf.ponyo.jponyo.common.midi;

import javax.sound.midi.ShortMessage;

/**
 * see: http://www.midi.org/techspecs/midimessages.php
 * 
 * ShortMessage Constants:
 * NOTE_ON  ... 144
 * NOTE_OFF ... 128
 * CONTROL_CHANGE ... 176
 */
public enum MidiMessageStatus {
	
	// channels reserved for controllers (channel 1-16): 176 to 191
	NOTE_ON_CHANNEL_1 (ShortMessage.NOTE_ON +  0 /* == 144 */),
	NOTE_ON_CHANNEL_2 (ShortMessage.NOTE_ON +  1 /* == 145 */),
	NOTE_ON_CHANNEL_3 (ShortMessage.NOTE_ON +  2 /* == 146 */),
	NOTE_ON_CHANNEL_4 (ShortMessage.NOTE_ON +  3 /* == 147 */),
	NOTE_ON_CHANNEL_5 (ShortMessage.NOTE_ON +  4 /* == 148 */),
	NOTE_ON_CHANNEL_6 (ShortMessage.NOTE_ON +  5 /* == 149 */),
	NOTE_ON_CHANNEL_7 (ShortMessage.NOTE_ON +  6 /* == 150 */),
	NOTE_ON_CHANNEL_8 (ShortMessage.NOTE_ON +  7 /* == 151 */),
	NOTE_ON_CHANNEL_9 (ShortMessage.NOTE_ON +  8 /* == 152 */),
	NOTE_ON_CHANNEL_10(ShortMessage.NOTE_ON +  9 /* == 153 */),
	NOTE_ON_CHANNEL_11(ShortMessage.NOTE_ON + 10 /* == 154 */),
	NOTE_ON_CHANNEL_12(ShortMessage.NOTE_ON + 11 /* == 155 */),
	NOTE_ON_CHANNEL_13(ShortMessage.NOTE_ON + 12 /* == 156 */),
	NOTE_ON_CHANNEL_14(ShortMessage.NOTE_ON + 13 /* == 157 */),
	NOTE_ON_CHANNEL_15(ShortMessage.NOTE_ON + 14 /* == 158 */),
	NOTE_ON_CHANNEL_16(ShortMessage.NOTE_ON + 15 /* == 159 */),
	
	CONTROLLER_CHANNEL_1 (ShortMessage.CONTROL_CHANGE +  0 /* == 176 */),
	CONTROLLER_CHANNEL_2 (ShortMessage.CONTROL_CHANGE +  1 /* == 177 */),
	CONTROLLER_CHANNEL_3 (ShortMessage.CONTROL_CHANGE +  2 /* == 178 */),
	CONTROLLER_CHANNEL_4 (ShortMessage.CONTROL_CHANGE +  3 /* == 179 */),
	CONTROLLER_CHANNEL_5 (ShortMessage.CONTROL_CHANGE +  4 /* == 180 */),
	CONTROLLER_CHANNEL_6 (ShortMessage.CONTROL_CHANGE +  5 /* == 181 */),
	CONTROLLER_CHANNEL_7 (ShortMessage.CONTROL_CHANGE +  6 /* == 182 */),
	CONTROLLER_CHANNEL_8 (ShortMessage.CONTROL_CHANGE +  7 /* == 183 */),
	CONTROLLER_CHANNEL_9 (ShortMessage.CONTROL_CHANGE +  8 /* == 184 */),
	CONTROLLER_CHANNEL_10(ShortMessage.CONTROL_CHANGE +  9 /* == 185 */),
	CONTROLLER_CHANNEL_11(ShortMessage.CONTROL_CHANGE + 10 /* == 186 */),
	CONTROLLER_CHANNEL_12(ShortMessage.CONTROL_CHANGE + 11 /* == 187 */),
	CONTROLLER_CHANNEL_13(ShortMessage.CONTROL_CHANGE + 12 /* == 188 */),
	CONTROLLER_CHANNEL_14(ShortMessage.CONTROL_CHANGE + 13 /* == 189 */),
	CONTROLLER_CHANNEL_15(ShortMessage.CONTROL_CHANGE + 14 /* == 190 */),
	CONTROLLER_CHANNEL_16(ShortMessage.CONTROL_CHANGE + 15 /* == 191 */);
	
//	private static final Map<Integer, MidiMessageStatus> ENUM_BY_RAW_VALUE = new HashMap<Integer, MidiMessageStatus>();
//	static {
//		for(MidiMessageStatus status : MidiMessageStatus.values()) {
//			ENUM_BY_RAW_VALUE.put(status.value, status);
//		}
//	}

	private final int value;
	
	private MidiMessageStatus(final int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}

	public static MidiMessageStatus controlByChannel(final int channel) {
		return MidiMessageStatus.byChannel(channel, ShortMessage.CONTROL_CHANGE);
	}

	public static MidiMessageStatus noteByChannel(final int channel) {
		return MidiMessageStatus.byChannel(channel, ShortMessage.NOTE_ON);
	}
	
	private static MidiMessageStatus byChannel(final int channel, final int offset) {
		if(channel < 0 || channel > 15) {
			throw new IllegalArgumentException("Channel out of range [" + channel + "]!");
		}
		for (final MidiMessageStatus status : MidiMessageStatus.values()) {
			final int currentChannel = status.getValue() - offset;
			if(currentChannel == channel) {
				return status;
			}
		}
		throw new RuntimeException("Unkown channel [" + channel + "] with offset [" + offset + "]!");
	}
	
	public boolean equalsTo(final ShortMessage message) {
		if(message == null) { throw new IllegalArgumentException("message == null"); }
		return message.getStatus() == this.value;
	}
	
	public static boolean isNoteMessage(final ShortMessage message) {
		final int status = message.getStatus();
		return status >= NOTE_ON_CHANNEL_1.getValue() && status <= NOTE_ON_CHANNEL_16.getValue();
	}
	
	@Override
	public String toString() {
		return "MidiMessageStatus[" + this.name() + ", value=" + this.value + "]";
	}
}
