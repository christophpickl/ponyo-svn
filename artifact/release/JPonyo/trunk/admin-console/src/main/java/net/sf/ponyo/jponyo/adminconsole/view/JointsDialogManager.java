package net.sf.ponyo.jponyo.adminconsole.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;

public class JointsDialogManager implements MotionStreamListener {

	private static final Log LOG = LogFactory.getLog(JointsDialogManager.class);
	
	private JointsDialog jointsDialog;
	
	private final Component parent;
	
	
	public JointsDialogManager(Component parent) {
		this.parent = parent;
	}

	public void toggleDialog() {
		if(this.jointsDialog == null) {
			this.setVisible(true);
		} else {
			this.setVisible(!this.jointsDialog.isVisible());
		}
	}

	public void onMotion(MotionData data) {
		if(this.jointsDialog != null) {
			this.jointsDialog.onMotion(data);
		}
	}

	public void setVisible(boolean visible) {
		
		if(this.jointsDialog != null) {
			if(visible == this.jointsDialog.isVisible()) {
				LOG.warn("Visibility already set to: " + visible);
			} else {
				this.jointsDialog.setVisible(visible);
			}
			
		} else { // jointsDialog == null
			this.jointsDialog = new JointsDialog();
			Dimension windowSize = this.parent.getSize();
			Point windowLocation = this.parent.getLocation();
			this.jointsDialog.setLocation(windowLocation.x + windowSize.width + 5, windowLocation.y);
			this.jointsDialog.setVisible(true);
		}
		
	}

	public void dispose() {
		if(this.jointsDialog != null) {
			this.jointsDialog.dispose();
		}
	}
}
