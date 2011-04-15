package net.sf.ponyo.midirouter.refactor;

import net.pulseproject.commons.midi.entity.ControllerMessage;
import net.sf.josceleton.Josceleton;
import net.sf.josceleton.core.api.entity.joint.Joint;
import net.sf.josceleton.core.api.entity.joint.Skeleton;
import net.sf.josceleton.core.api.entity.location.Coordinate;
import net.sf.josceleton.core.api.entity.location.Direction;
import net.sf.josceleton.core.api.entity.location.Range;
import net.sf.josceleton.core.api.entity.location.RangeScaler;
import net.sf.josceleton.prototype.midi.util.LogUtil;
import net.sf.josceleton.prototype.midi.util.SomeUtil;

public class MidiMapping {
	
	private static final RangeScaler SCALER = Josceleton.getRangeScaler();
	
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
	
	public ControllerMessage buildMidiMessage(Coordinate coord, Skeleton skeleton) {
		float coordValue = this.direction.extractValue(coord);
		if(this.relativeToJoint != null) {
			if(skeleton.isCoordinateAvailable(this.relativeToJoint) == false) {
				System.err.println("Could not build MIDI message because of unsufficient skeleton data for joint [" + this.relativeToJoint.getLabel() + "]!");
				return null; // ouch
			}
			final float relativeCoordValue = this.direction.extractValue(skeleton.getNullSafe(this.relativeToJoint));
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
