package net.sf.ponyo.midirouter.logic.midi;

import net.sf.ponyo.jponyo.common.geom.Range;
import net.sf.ponyo.jponyo.entity.Direction;
import net.sf.ponyo.jponyo.entity.Joint;


public class MidiMapping {

	private final Joint joint;
	private final Direction direction;
	private final Range range;
	private final int midiChannel;
	private final int controllerNumber;
	private final Joint relativeToJoint;
	
//	private double recentReceivedData; ... raw data by openni
//	private double recentSentData; ... scaled by range

	public MidiMapping(Joint joint, Direction direction, Range range, int midiChannel, int controllerNumber, Joint relativeToJoint) {
		this.joint = joint;
		this.direction = direction;
		this.range = range;
		this.midiChannel = midiChannel;
		this.controllerNumber = controllerNumber;
		this.relativeToJoint = relativeToJoint;
	}
	
	public final Joint getJoint() {
		return this.joint;
	}
	public Direction getDirection() {
		return this.direction;
	}
	public Range getRange() {
		return this.range;
	}
	public int getMidiChannel() {
		return this.midiChannel;
	}
	public int getControllerNumber() {
		return this.controllerNumber;
	}
	public Joint getRelativeToJoint() {
		return this.relativeToJoint;
	}

	@Override public String toString() {
		return "MidiMapping[joint="+this.joint+", direction="+this.direction+"]";
	}
}
