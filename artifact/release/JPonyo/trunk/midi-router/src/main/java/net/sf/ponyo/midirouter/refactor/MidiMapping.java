package net.sf.ponyo.midirouter.refactor;

import net.sf.ponyo.jponyo.common.geom.Range;
import net.sf.ponyo.jponyo.common.geom.RangeScaler;
import net.sf.ponyo.jponyo.common.math.Array3f;
import net.sf.ponyo.jponyo.common.midi.ControllerMessage;
import net.sf.ponyo.jponyo.entity.Direction;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.entity.Skeleton;

public class MidiMapping {
	
	private static final RangeScaler SCALER = new RangeScaler();
	
	private final Joint joint;
	private final Direction direction;
	private final Range range;
	private final int midiChannel;
	private final int controllerNumber;
	private final Joint relativeToJoint;
	private int currentLogCount = 0;

	public MidiMapping(Joint joint, Direction direction, Range range, int midiChannel, int controllerNumber, Joint relativeToJoint) {
		this.joint = joint;
		this.direction = direction;
		this.range = range;
		this.midiChannel = midiChannel;
		this.controllerNumber = controllerNumber;
		this.relativeToJoint = relativeToJoint;
	}

	public boolean appliesPart(final Joint comparingPart) {
		return this.joint == comparingPart;
	}
	
	public ControllerMessage buildMidiMessage(Array3f coord, Skeleton skeleton) {
		float coordValue = this.direction.extractValue(coord);
		if(this.relativeToJoint != null) {
			// FIXME
//			if(skeleton.isCoordinateAvailable(this.relativeToJoint) == false) {
//				System.err.println("Could not build MIDI message because of unsufficient skeleton data for joint [" + this.relativeToJoint.getLabel() + "]!");
//				return null; // ouch
//			}
			Array3f coordinates = skeleton.getCoordinates(this.relativeToJoint);
			final float relativeCoordValue = this.direction.extractValue(coordinates);
			coordValue = relativeCoordValue - coordValue;
			
		}
		final int controllerValue = SCALER.scale(coordValue, this.range);
		
		this.currentLogCount++;
		if(this.currentLogCount == LogUtil.LOG_JOINT_EVERY) {
//			final int prettyCoordValue = Math.round(coordValue * 100);
			this.currentLogCount = 0;
			LogUtil.log("Captured " +
					SomeUtil.fillString(this.joint.getLabel(), 12) + " (coord: " + coordValue + ") -> " +
					"MIDI ch |ctl|val: " +
						this.midiChannel + " | " +
						this.controllerNumber + " | " +
						controllerValue);
		}
		
		return new ControllerMessage(this.midiChannel, this.controllerNumber, controllerValue);
	}
	
	
	@Override public String toString() {
		return "On " + this.joint.getLabel() +" in "+this.direction+"-direction " +
				"with MIDI settings "+this.midiChannel+"/"+this.controllerNumber;
	}
}
