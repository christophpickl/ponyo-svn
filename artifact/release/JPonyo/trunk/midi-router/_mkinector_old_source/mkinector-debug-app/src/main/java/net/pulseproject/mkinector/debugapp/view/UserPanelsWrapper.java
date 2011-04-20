package net.pulseproject.mkinector.debugapp.view;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.pulseproject.mkinector.debugapp.misc.OscConnectionWindowGlueListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserPanelsWrapper extends JPanel implements OscConnectionWindowGlueListener {

	private static final long serialVersionUID = -838314886028159693L;
	
	private static final Log LOG = LogFactory.getLog(UserPanelsWrapper.class);
	
	public UserPanelsWrapper() {
		this.setOpaque(false);
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final int gap = 10;
		this.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
	}

	@Override
	public final void onAddUserPanel(final UserPanel userPanel) {
		LOG.debug("onAddUserPanel(userPanel)");
		this.add(userPanel.asComponent());
	}

	@Override
	public final void onRemoveUserPanel(final UserPanel userPanel) {
		LOG.debug("onRemoveUserPanel(userPanel)");
		this.remove(userPanel.asComponent());
	}

	@Override
	public final void onUserCountChanged(final int userReadyCount, final int userWaitingCount) {
		// nothing to do
	}
}
