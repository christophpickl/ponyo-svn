package net.sf.ponyo.jponyo.samples;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import net.sf.ponyo.jponyo.JPonyoModule;
import net.sf.ponyo.jponyo.common.gui.GuiUtil;
import net.sf.ponyo.jponyo.common.log.LogUtil;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.pose.Pose;
import net.sf.ponyo.jponyo.pose.PoseListener;
import net.sf.ponyo.jponyo.pose.PoseRule;
import net.sf.ponyo.jponyo.pose.PsiPose;
import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class PoseSample {
	
	private final ContextStarter starter;
	final Map<PoseRule, JLabel> ruleLabels = new HashMap<PoseRule, JLabel>();
	
	public static void main(String[] args) {
		LogUtil.ensureDefaultLogger();
		Injector injector = Guice.createInjector(new JPonyoModule());
		injector.getInstance(PoseSample.class).start();
	}

	@Inject
	public PoseSample(ContextStarter starter) {
		this.starter = starter;
	}
	
	private void start() {
		final Pose pose = new PsiPose();
		for(PoseRule rule : pose.getRules()) {
			this.ruleLabels.put(rule, new JLabel("--------"));
		}
		pose.addListener(new PoseListener() {
			public void onPoseRuleChanged(PoseRule rule) {
				PoseSample.this.ruleLabels.get(rule).setText(rule.getLabel() + " ==> " + rule.isActive());
			}
			public void onPoseLeft() {
				System.out.println("!!!!!!!!!!! LEFT");
			}
			public void onPoseEntered() {
				System.out.println("!!!!!!!!!!! ENTERED");
			}
		});
		
		final Context context = this.starter.startOscReceiver();
		context.getContinuousUserProvider().addListener(new ContinuousUserListener() {
			public void onCurrentUserChanged(User user) {
				System.out.println("onCurrentUserChanged(user="+user+")");
				if(user == null && pose.isDetecting()) {
					pose.stopDetecting();
					return;
				}
				if(pose.isDetecting() == false) {
					pose.startDetecting(user, context.getMotionStream());
				}
			}
		});
		
		final JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				f.dispose();
				if(pose.isDetecting()) {
					pose.stopDetecting();
				}
				context.shutdown();
			}
		});
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		for(JLabel lbl : this.ruleLabels.values()) {
			p.add(lbl);
		}
		f.getContentPane().add(p);
		f.setSize(700, 300);
		GuiUtil.setCenterLocation(f);
		f.setVisible(true);
	}
	
}
