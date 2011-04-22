package net.sf.ponyo.jponyo.pose;

import java.util.Collection;

import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.samples.PoseSample;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStream;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractPose
	extends DefaultAsync<PoseListener>
		implements Pose, MotionStreamListener {

	private static final Log LOG = LogFactory.getLog(AbstractPose.class);
	
	private final String label;
	private final Collection<PoseRule> rules;
	private User user;
	private MotionStream stream;
	private boolean detecting = false;
	private boolean allRulesActive = false;
	
	public AbstractPose(String label, Collection<PoseRule> rules) {
		this.label = label;
		this.rules = rules;
	}

	public final void onMotion(MotionData data) {
//		System.out.println("xxxxx!");
		this.validateRules();
	}
	
	private void validateRules() {
		boolean currentlyAllRulesActive = true;
		for (PoseRule rule : this.rules) {
			boolean hasChanged = rule.validateAndHasChanged(this.user.getSkeleton());
			if(hasChanged) {
				this.dispatchPoseRuleChanged(rule);
			}
			if(rule.isActive() == false) {
				currentlyAllRulesActive = false;
			}
			
//			if(rule.isActive() == false) { // TODO safe performance: stop on very first rule break!
//				break;
//			}
		}
		
		if(currentlyAllRulesActive != this.allRulesActive) {
			this.allRulesActive = currentlyAllRulesActive;
			if(this.allRulesActive) {
				this.dispatchPoseEntered();
			} else {
				this.dispatchPoseLeft();
			}
		}
	}

	public final void startDetecting(User newUser, MotionStream newStream) {
		if(this.detecting == true) {
			throw new IllegalStateException("Yet started for user: " + this.user);
		}
		
		this.user = newUser;
		this.stream = newStream;
		this.stream.addListenerFor(this.user, this);
		this.detecting = true;
	}
	
	public final void stopDetecting() {
		if(this.detecting == false) {
			LOG.warn("Not stopping, as not yet started!");
			return;
		}
		
		this.stream.removeListenerFor(this.user, this);
		this.user = null;
		this.stream = null;
		this.detecting = false;
	}

	public boolean isDetecting() {
		return this.detecting;
	}

	public final String getLabel() {
		return this.label;
	}

	public final Collection<PoseRule> getRules() {
		return this.rules;
	}

	
	private void dispatchPoseRuleChanged(PoseRule rule) {
		for (PoseListener listener : this.getListeners()) {
			listener.onPoseRuleChanged(rule);
		}
	}
	private void dispatchPoseEntered() {
		for (PoseListener listener : this.getListeners()) {
			listener.onPoseEntered();
		}
	}
	private void dispatchPoseLeft() {
		for (PoseListener listener : this.getListeners()) {
			listener.onPoseLeft();
		}
	}
	
}
