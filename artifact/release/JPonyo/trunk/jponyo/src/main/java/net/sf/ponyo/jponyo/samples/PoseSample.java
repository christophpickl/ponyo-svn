package net.sf.ponyo.jponyo.samples;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import net.sf.ponyo.jponyo.JPonyoModule;
import net.sf.ponyo.jponyo.common.gui.GuiUtil;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.pose.Pose;
import net.sf.ponyo.jponyo.pose.PsiPose;
import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class PoseSample {
	
	private static PoseSample hack;

	private final ContextStarter starter;
	public static final JLabel LBL_1 = new JLabel("...");
	
	public static void HACK_foo(JLabel lbl, String msg) {
		if(hack != null) {
			lbl.setText(msg);
		}
	}
	
	public static void main(String[] args) {
		Logger log4jLog = Logger.getRootLogger();
		if(log4jLog.getAllAppenders().hasMoreElements() == false) {
			PatternLayout layout = new PatternLayout("%d [%t] [%-5p] %-30c{1} - %m%n");
			Appender newAppender = new ConsoleAppender(layout);
			log4jLog.addAppender(newAppender);
		}
		
		Injector injector = Guice.createInjector(new JPonyoModule());
		injector.getInstance(PoseSample.class).start();
	}

	@Inject
	public PoseSample(ContextStarter starter) {
		this.starter = starter;
		hack = this;
	}
	
	private void start() {
		final Pose pose = new PsiPose();
		
		final Context context = this.starter.startOscReceiver();
		context.getContinuousUserProvider().addListener(new ContinuousUserListener() {
			public void onCurrentUserChanged(User user) {
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
		p.add(this.LBL_1);
		f.getContentPane().add(p);
		f.setSize(700, 300);
		GuiUtil.setCenterLocation(f);
		f.setVisible(true);
		
	}
	
}
