package net.sf.ponyo.jponyo.common.midi;

public class ControllerMessage extends MessageTemplate {
	
	public ControllerMessage(final int midiChannel, final int controllerNumber, final int controllerValue) {
		super(MidiMessageStatus.controlByChannel(midiChannel), controllerNumber, controllerValue);
	}
	
	public static ControllerMessage bySocket(final MidiSocket socket, final int controllerValue) {
		if(socket == null) { throw new IllegalArgumentException("socket == null"); }
		return new ControllerMessage(socket.getChannel().intValue(), socket.getController().intValue(),
				controllerValue);
	}
}
