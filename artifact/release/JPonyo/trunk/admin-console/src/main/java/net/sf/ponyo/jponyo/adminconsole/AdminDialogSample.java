package net.sf.ponyo.jponyo.adminconsole;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.AdminDialog;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserState;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class AdminDialogSample {
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AdminConsoleModule());
		injector.getInstance(AdminDialogSample.class).start();
	}
	
	private final ContextStarter contextStarter;

	@Inject
	public AdminDialogSample(ContextStarter contextStarter) {
		this.contextStarter = contextStarter;
	}
	
	public void start() {
		final Context context = this.contextStarter.startOscReceiver();
		
		final AdminDialog dialog = new AdminDialog();
		dialog.onCurrentUserChanged(context.getContinuousUserProvider().getCurrentUser());
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
	
	void onClose(Context context, AdminDialog dialog) {
		dialog.setVisible(false);
		dialog.dispose();
		
		context.getContinuousMotionStream().removeListener(dialog);
		context.getContinuousUserProvider().removeListener(dialog);
		context.shutdown();
		
//		System.exit(0);
	}
}
