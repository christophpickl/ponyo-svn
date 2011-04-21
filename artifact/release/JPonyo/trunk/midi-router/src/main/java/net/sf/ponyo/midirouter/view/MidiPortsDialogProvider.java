package net.sf.ponyo.midirouter.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.midirouter.logic.Model;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class MidiPortsDialogProvider implements Provider<MidiPortsDialog> {

	private static final Log LOG = LogFactory.getLog(MidiPortsDialogProvider.class);
	private final Model model;
	
	@Inject
	public MidiPortsDialogProvider(Model model) {
		this.model = model;
	}

	public MidiPortsDialog get() {
		LOG.debug("get()");
		return new MidiPortsDialog(this.model);
	}

}
