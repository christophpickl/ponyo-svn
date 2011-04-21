package net.sf.ponyo.midirouter.refactor;

import java.util.Collection;

import net.sf.ponyo.jponyo.common.math.Array3f;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.entity.Skeleton;
import net.sf.ponyo.jponyo.stream.ContinuousMotionStream;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.User;

public class PrototypeLogic implements MotionStreamListener {
	
	private Context joscConnection;
	private final MidiConnection midiConnection;
	private Collection<MidiMapping> mappings;
	
	private ContinuousMotionStream cms;

//	new PrototypeLogic("IAC Driver - Chrisi A",
//			//           bodyPart              direction        midiChannel  controllerNumber
//		new MidiMapping(Body.HAND().LEFT(),   XyzDirection.X,  1,           1               ),
//		new MidiMapping(Body.HAND().LEFT(),   XyzDirection.Y,  1,           2               )
//	).startUp();
	
	public PrototypeLogic(String midiPort, Collection<MidiMapping> mappings) {
		this.midiConnection = new MidiConnection(midiPort);
		this.mappings = mappings;
	}
	
	public void open() { // TODO throws InvalidInputException {
		this.midiConnection.connect();
		
		this.joscConnection = null; // new ContextStarterImpl().startOscReceiver(); //Josceleton.openConnection();
		this.cms = this.joscConnection.getContinuousMotionStream(); //Josceleton.getContinuousMotionStreamFactory().create(this.joscConnection);
		// add user listener and display to user
		this.cms.addListener(this);
	}
	
	public void close() {
		if(this.joscConnection != null) {
			this.cms.removeListener(this);
			this.joscConnection.shutdown();
			this.midiConnection.close();
			this.joscConnection = null;
		}
	}
	
	public Context getJoscConnection() {
		return this.joscConnection;
	}

	public synchronized void updateMappings(final Collection<MidiMapping> mappings) {
		this.mappings = mappings;
	}
	
//	public synchronized void onMoved(Joint part, Coordinate updatedCoordinate, Skeleton skeleton) {

	public void onMotion(MotionData data) {
		Joint part = data.getJoint();
		User user = data.getUser();
		Array3f updatedCoordinate = data.getJointPosition();
		Skeleton skeleton = user.getSkeleton();
		
		for (final MidiMapping map : this.mappings) {
			if(map.appliesPart(part) == false) {
				continue;
			}
			
//			final ControllerMessage midiMsg = map.buildMidiMessage(updatedCoordinate, skeleton);
//			if(midiMsg != null) {
//				this.midiConnection.send(midiMsg);
//			}
		}
	}
}
