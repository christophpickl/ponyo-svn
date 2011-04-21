package net.sf.ponyo.midirouter.logic;

import java.util.Collection;

import net.pulseproject.commons.midi.entity.ControllerMessage;
import net.sf.ponyo.jponyo.adminconsole.view.AdminDialog;
import net.sf.ponyo.jponyo.common.io.IoUtil;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserState;
import net.sf.ponyo.midirouter.logic.midi.MidiConnection;
import net.sf.ponyo.midirouter.logic.midi.MidiConnector;
import net.sf.ponyo.midirouter.logic.midi.MidiMapping;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;

class RouterServiceImpl implements MotionStreamListener, UserChangeListener, RouterService {
	
	private static final Log LOG = LogFactory.getLog(RouterServiceImpl.class);
	
	private final Model model;
	private final ContextStarter contextStarter;
	private final MidiConnector midiConnector;
	private final MessageTransformer transformer = new MessageTransformer();
	
	private Context ponyoContext;
	private MidiConnection midiConnection;
	
	private MidiMappings mappings;
	private User recentUser;
	private AdminDialog adminDialog;
	
	
	@Inject
	public RouterServiceImpl(Model model, ContextStarter contextStarter, MidiConnector midiConnector) {
		this.model = model;
		this.contextStarter = contextStarter;
		this.midiConnector = midiConnector;
	}

	public void start() {
		LOG.info("start()");
		
		this.mappings = this.model.getActiveMappings();
		String midiPort = this.model.getMidiPort();
		
		this.midiConnection = this.midiConnector.openConnection(midiPort);
		if(this.midiConnection == null) {
			throw new RuntimeException("Could not find MIDI port '" + midiPort +"'!");
		}
		
		try {
			this.ponyoContext = this.contextStarter.startOscReceiver();
		} catch(Exception e) {
			this.safeClose();
			throw new RuntimeException("Could not open JPonyo connection!", e);
		}
		
		this.ponyoContext.getContinuousMotionStream().addListener(this);
		this.ponyoContext.addUserChangeListener(this);
		
		LOG.info("Connection established!");
	}
	
	public void stop() {
		LOG.info("stop()");
		this.safeClose();
	}
	
	private void safeClose() {
		if(this.ponyoContext != null) {
			this.ponyoContext.getContinuousMotionStream().removeListener(this);
			this.ponyoContext.removeUserChangeListener(this);
			this.ponyoContext.shutdown();
			this.ponyoContext = null;
		}
		
		if(this.adminDialog != null) {
			this.adminDialog.setUser(null);
		}
		
//		MINOR PonyoContext should implement Closeable, so to be used by IoUtil.close(this.ponyoContext);
		IoUtil.close(this.midiConnection);
		this.mappings = null;
	}
	
	public synchronized void onMotion(MotionData data) {
		for (final MidiMapping map : this.mappings.getMappings()) {
			if(data.getJoint() != map.getJoint()) {
				continue;
			}
			
			ControllerMessage message = this.transformer.transformAndUpdate(data, map);
			this.midiConnection.send(message.build());
			this.model.setFrameCount(Integer.valueOf(this.model.getFrameCount().intValue() + 1));
		}
		
	}

	public void manage(AdminDialog adminDialogToManager) {
		this.adminDialog = adminDialogToManager;
	}
	
	private void checkUsers() {
		Collection<User> filteredUsers = this.ponyoContext.getGlobalSpace().getFilteredUsers(UserState.TRACKING);
		
		if(filteredUsers.isEmpty() == true) {
			this.adminDialog.setUser(null);
			this.recentUser = null;
		} else {
			User newUser = filteredUsers.iterator().next();
			this.adminDialog.setUser(newUser);
			this.recentUser = newUser;
		}
	}
	
	public void onUserChanged(User user, UserState state) {
		LOG.debug("onUserChanged(user="+user+", state="+state+")");
		
		if(this.recentUser == null && state == UserState.TRACKING) {
			this.adminDialog.setUser(user);
			this.recentUser = user;
			
		} else if(this.recentUser == user && state == UserState.LOST) {
			this.checkUsers();
		}
	}

	public synchronized void updateMappings() {
		LOG.debug("updateMappings()");
		this.mappings = this.model.getActiveMappings();
	}
	
}
