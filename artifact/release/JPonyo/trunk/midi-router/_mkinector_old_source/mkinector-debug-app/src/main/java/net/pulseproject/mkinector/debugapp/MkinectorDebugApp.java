package net.pulseproject.mkinector.debugapp;

import javax.swing.SwingUtilities;

import net.pulseproject.josceleton.Josceleton;
import net.pulseproject.mkinector.debugapp.misc.OscConnectionWindowGlue;
import net.pulseproject.mkinector.debugapp.misc.PlainSimpleMidiSenderImpl;
import net.pulseproject.mkinector.debugapp.scenario1.Scenario1;
import net.pulseproject.mkinector.debugapp.scenario1.SimpleMidiSender;
import net.pulseproject.mkinector.debugapp.view.MainWindow;
import net.pulseproject.mkinector.debugapp.view.MainWindowListener;
import net.pulseproject.mkinector.debugapp.view.UserPanelFactory;
import net.pulseproject.mkinector.josceleton.api.JosceletonConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class MkinectorDebugApp implements MainWindowListener {
	
	private static final Log LOG = LogFactory.getLog(MkinectorDebugApp.class);
	
	private JosceletonConnection connection;
	
	
	public static void main(final String[] args) {
		final MkinectorDebugApp app = new MkinectorDebugApp();
		app.startUp();
	}
	
	public final void startUp() {
		LOG.info("startUp()");
		
		final MainWindow window = new MainWindow(this);
		
		final Injector injector = Guice.createInjector(new MkinectorDebugAppGuiceModule());
		final UserPanelFactory userPanelFactory = injector.getInstance(UserPanelFactory.class);
		final OscConnectionWindowGlue windowGlue = new OscConnectionWindowGlue(userPanelFactory, window);
		
		this.connection = Josceleton.openConnection();
		this.connection.addListener(windowGlue);
		Josceleton.addUserManagerListener(windowGlue);
		
//		final UserCollection users = this.connection.getUserCollection();
//		windowGlue.initWithUsers(users);
//		DELME this.connection.addListener(windowGlue);
		
		
		final int noteChannel = 0;
		final SimpleMidiSender midiSender = new PlainSimpleMidiSenderImpl(noteChannel);
		final Scenario1 scenario = new Scenario1(this.connection);
		scenario.registerGestures(midiSender);
		
		
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
			window.setVisible(true);	
		}});
	}
	
	@Override
	public final void onQuit() {
		LOG.debug("onQuit()");
		
		this.connection.close();
		this.connection = null;
	}

}
