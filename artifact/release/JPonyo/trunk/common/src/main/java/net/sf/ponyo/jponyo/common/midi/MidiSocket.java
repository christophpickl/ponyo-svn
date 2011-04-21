package net.sf.ponyo.jponyo.common.midi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiSocket {
	
	private static final Log LOG = LogFactory.getLog(MidiSocket.class);
	
	private static final int IGNORE_CONTROLLER_INT = -42;
	
	public static final Integer IGNORE_CONTROLLER = Integer.valueOf(IGNORE_CONTROLLER_INT);
	
	private final String port;
	
	private final Integer channel;
	
	private final Integer controller;
	
	
	public MidiSocket(final String port, final Integer channel) {
		this(port, channel, IGNORE_CONTROLLER);
	}
	
	/**
	 * @param port MIDI port name, eg: "IAC Driver - Foobar"
	 * @param channel something between [0..15]
	 * @param controller either is IGNORE_CONTROLLER_INT or between [0..127]
	 */
	public MidiSocket(final String port, final Integer channel, final Integer controller) {
		LOG.debug("new MidiSocket(port="+port+", channel="+channel+", controller="+controller+")");
		if(port == null) { throw new IllegalArgumentException("port == null"); }
		if(port.isEmpty()) { throw new IllegalArgumentException("port is empty"); }
		this.port = port;
		
		if(channel == null) { throw new IllegalArgumentException("channel == null"); }
		final int channelInt = channel.intValue();
		if(channelInt < 0 || channelInt > 15) {
			throw new IllegalArgumentException("channel out of range: " + channel);
		}
		this.channel = channel;
		
		if(controller == null) { throw new IllegalArgumentException("controller == null"); }
		final int controllerInt = controller.intValue();
		if(controllerInt != IGNORE_CONTROLLER_INT &&
				controllerInt < 0 || controllerInt > 127) {
			throw new IllegalArgumentException("controller number out of range: " + controller);
		}
		this.controller = controller;
	}

	public final String getPort() {
		return this.port;
	}

	public final Integer getChannel() {
		return this.channel;
	}
	
	public final Integer getController() {
		return this.controller;
	}
	
	@Override
	public final boolean equals(final Object other) {
		if ((other instanceof MidiSocket) == false) {
			return false;
		}
		final MidiSocket that = (MidiSocket) other;
		return this.port.equals(that.port) &&
			   this.channel.equals(that.channel) &&
			   this.controller.equals(that.controller);
	}
	
	@Override
	public final int hashCode() {
		return this.port.hashCode() + this.channel.hashCode();
	}
	
	@Override
	public final String toString() {
		return "MidiSocket[" +
				"port=[" + this.port + "], " +
				"channel=" + this.channel + ", " +
				"controller=" + this.controller + "]";
	}
	
//	public static MidiSocket valueOf(final String port, final Integer channel, final Integer controller) {
//		cache
//	}
}
