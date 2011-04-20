package net.sf.ponyo.midirouter.logic;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import net.sf.ponyo.jponyo.common.io.IoUtil;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.midirouter.logic.midi.MidiConnection;
import net.sf.ponyo.midirouter.logic.midi.MidiConnector;
import net.sf.ponyo.midirouter.logic.midi.MidiMapping;
import net.sf.ponyo.midirouter.logic.midi.MidiMappings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RouterService implements MotionStreamListener {
	
	private static final Log LOG = LogFactory.getLog(RouterService.class);
	
	private final MidiConnector midiConnector = new MidiConnector();
	private Context ponyoContext;
	
	private MidiConnection midiConnection;
	private final MessageTransformer transformer = new MessageTransformer();
	private MidiMappings mappings;
	
	public void start(String midiPort, MidiMappings startMappings) {
		LOG.info("start(..)");
		this.mappings = startMappings;
		
		try {
			this.midiConnection = this.midiConnector.openConnection(midiPort);
		} catch (MidiUnavailableException e) {
			throw new RuntimeException("Could not open MIDI port '" + midiPort +"'!", e);
		}
		if(this.midiConnection == null) {
			throw new RuntimeException("Could not find MIDI port '" + midiPort +"'!");
		}
		
		try {
			this.ponyoContext = new ContextStarter().startOscReceiver();
		} catch(Exception e) {
			this.safeClose();
			throw new RuntimeException("Could not open JPonyo connection!", e);
		}
		
		this.ponyoContext.getContinuousMotionStream().addListener(this);
		
		LOG.info("Connection established!");
	}
	
	public void stop() {
		LOG.info("stop()");
		if(this.ponyoContext != null) {
			this.ponyoContext.getContinuousMotionStream().removeListener(this);
		}
		this.safeClose();
	}
	
	private void safeClose() {
		this.ponyoContext.shutdown();
		
//		TODO IoUtil.close(this.ponyoContext);
		IoUtil.close(this.midiConnection);
		this.mappings = null;
	}

	public void onMotion(MotionData data) {
		for (final MidiMapping map : this.mappings.getMappings()) {
			if(data.getJoint() != map.getJoint()) {
				continue;
			}
			ShortMessage message = this.transformer.transform(data, map);
			this.midiConnection.send(message);
		}
		
	}
	
}
