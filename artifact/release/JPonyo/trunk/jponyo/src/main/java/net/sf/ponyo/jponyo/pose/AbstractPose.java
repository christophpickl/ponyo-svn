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
	
	
	public AbstractPose(String label, Collection<PoseRule> rules) {
		this.label = label;
		this.rules = rules;
	}

	public final void onMotion(MotionData data) {
		this.validateRules();
	}
	
	private void validateRules() {
		int i = 1;
		for (PoseRule rule : this.rules) {
			boolean ruleResult = rule.validate(this.user.getSkeleton());
//			if(ruleResult == false) { // TODO safe performance: stop on very first rule break!
//				break;
//			}
			switch(i) {
				case 1:
					PoseSample.HACK_foo(PoseSample.LBL_1, rule.getLabel() + " => " + ruleResult);
					break;
			}
//			System.out.println("rule " + rule.getLabel() + " ===> " + ruleResult);
			i++;
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
}
