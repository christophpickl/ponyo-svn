package net.sf.ponyo.midirouter.logic;

import javax.sound.midi.ShortMessage;

import net.pulseproject.commons.midi.entity.ControllerMessage;
import net.sf.ponyo.jponyo.common.geom.RangeScaler;
import net.sf.ponyo.jponyo.common.math.Array3f;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.midirouter.logic.midi.MidiMapping;

public class MessageTransformer {
	
	private final RangeScaler scaler = new RangeScaler();
	
	public ShortMessage transform(MotionData data, MidiMapping map) {
		assert(data.getJoint() == map.getJoint());
		
		Array3f position = data.getJointPosition();
		float positionValue = map.getDirection().extractValue(position);
		
		Joint relativeToJoint = map.getRelativeToJoint();
		if(relativeToJoint != null) {
			Array3f relativePosition = data.getUser().getSkeleton().getCoordinates(relativeToJoint);
			float relativePositionValue = map.getDirection().extractValue(relativePosition);
			positionValue = relativePositionValue - positionValue;
		}
		
		int controllerValue = this.scaler.scale(positionValue, map.getRange());
		
		return new ControllerMessage(map.getMidiChannel(), map.getControllerNumber(), controllerValue).build();
	}

}
