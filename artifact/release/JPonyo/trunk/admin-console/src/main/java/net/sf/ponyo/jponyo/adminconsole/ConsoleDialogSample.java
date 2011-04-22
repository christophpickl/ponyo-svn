package net.sf.ponyo.jponyo.adminconsole;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.app.ConsoleModule;
import net.sf.ponyo.jponyo.adminconsole.view.ConsoleDialog;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class ConsoleDialogSample {
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ConsoleModule());
		injector.getInstance(ConsoleDialogSample.class).start();
	}
	
	private final ContextStarter contextStarter;

	@Inject
	public ConsoleDialogSample(ContextStarter contextStarter) {
		this.contextStarter = contextStarter;
	}
	
	public void start() {
		final Context context = this.contextStarter.startOscReceiver();
		
		final ConsoleDialog dialog = new ConsoleDialog();
		dialog.onCurrentUserChanged(context.getContinuousUserProvider().getCurrentUser()); // initialize user
		context.getContinuousMotionStream().addListener(dialog);
		context.getContinuousUserProvider().addListener(dialog);
		
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowevent) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						onClose(context, dialog);
					}
				});
			}
		});
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dialog.setVisible(true);
			}
		});
	}
	
	void onClose(Context context, ConsoleDialog dialog) {
		dialog.setVisible(false);
		dialog.dispose();
		
		context.getContinuousMotionStream().removeListener(dialog);
		context.getContinuousUserProvider().removeListener(dialog);
		context.shutdown();
		
//		System.exit(0);
	}
}
