package net.sf.ponyo.midirouter.refactor;

import java.util.Collection;

import net.pulseproject.commons.midi.entity.ControllerMessage;
import net.sf.ponyo.jponyo.connection.Connection;
import net.sf.ponyo.jponyo.user.User;

public class PrototypeLogic implements MotionStreamListener, UserServiceListener {
	
	private Connection joscConnection;
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
		
		this.joscConnection = Josceleton.openConnection();
		this.cms = Josceleton.getContinuousMotionStreamFactory().create(this.joscConnection);
		this.joscConnection.getUserService().addListener(this);
		this.cms.addListener(this);
	}
	
	public void close() {
		if(this.joscConnection != null) { // TODO check hack
			this.cms.removeListener(this);
			this.cms = null;
			
			this.joscConnection.getUserService().removeListener(this);
			this.joscConnection.close();
			this.joscConnection = null;
			
			this.midiConnection.close();
		}
	}
	
	public Connection getJoscConnection() {
		return this.joscConnection;
	}

	public synchronized void updateMappings(final Collection<MidiMapping> mappings) {
		this.mappings = mappings;
	}
	@Override
	public synchronized void onMoved(Joint part, Coordinate updatedCoordinate, Skeleton skeleton) {
		for (final MidiMapping map : this.mappings) {
			if(map.appliesPart(part) == false) {
				continue;
			}
			
			final ControllerMessage midiMsg = map.buildMidiMessage(updatedCoordinate, skeleton);
			if(midiMsg != null) {
				this.midiConnection.send(midiMsg);
			}
		}
	}

	@Override public void onUserWaiting(User user) { LogUtil.log("New waiting User: " + user.getOsceletonId()); }
	@Override public void onUserProcessing(User user) { LogUtil.log("New processing User: " + user.getOsceletonId()); }
	@Override public void onUserDead(User user) { LogUtil.log("Lost User: " + user.getOsceletonId()); }
	
}
