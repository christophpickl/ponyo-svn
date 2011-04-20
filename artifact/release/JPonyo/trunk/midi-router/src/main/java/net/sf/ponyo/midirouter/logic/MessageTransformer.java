package net.sf.ponyo.midirouter.logic;

import javax.sound.midi.ShortMessage;

import net.pulseproject.commons.midi.entity.ControllerMessage;
import net.sf.ponyo.jponyo.common.math.Array3f;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.midirouter.refactor.LogUtil;
import net.sf.ponyo.midirouter.refactor.SomeUtil;

public class MessageTransformer {

	public ShortMessage transform(MotionData data, MidiMapping map) {
		Joint joint = data.getJoint();
		Array3f position = data.getJointPosition();
		
		/*
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
		
		return new ControllerMessage(this.midiChannel, this.controllerNumber, controllerValue).build();
		*/
		return null;
	}

}
