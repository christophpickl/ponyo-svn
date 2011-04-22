package net.sf.ponyo.jponyo.adminconsole.app;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.JointsDialog;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.core.GlobalSpace;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class ConsoleApp
	implements ConsoleWindowListener, UserChangeListener {

	private static final Log LOG = LogFactory.getLog(ConsoleApp.class);
	
	private final ContextStarter contextStarter;
	private Context context;
	private GlobalSpace space;
	private ConsoleWindow window;
	private User recentUser;
	
	public static void main(String[] args) {
		LOG.debug("main() START");
		
		Injector injector = Guice.createInjector(new ConsoleModule());
		ConsoleApp app = injector.getInstance(ConsoleApp.class);
		app.startUp();
		
		LOG.debug("main() END");
	}

	@Inject
	public ConsoleApp(ContextStarter contextStarter) {
		this.contextStarter = contextStarter;
	}

	public void startUp() {
		this.context = this.contextStarter.startOscReceiver();
		this.space = this.context.getGlobalSpace();
		this.window = new ConsoleWindow(this);

		this.checkUsers();
		this.context.addUserChangeListener(this);
		
		this.context.getContinuousMotionStream().addListener(this.window);
		
	    this.window.setSize(800, 600);
	    SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ConsoleApp.this.onShowWindow();
			}
		});
	}
	
	void onShowWindow() {
		LOG.debug("displaying window");
		
		this.window.setVisible(true);
	}

	public void onQuit() {
		this.context.getContinuousMotionStream().removeListener(this.window);
		this.window.destroy();
		this.context.shutdown();
		
		System.exit(0);
	}
	
	private void checkUsers() {
		Collection<User> filteredUsers = this.space.getFilteredUsers(UserState.TRACKING);
		
		if(filteredUsers.isEmpty() == true) {
			this.window.setUser(null);
			this.recentUser = null;
		} else {
			User newUser = filteredUsers.iterator().next();
			this.window.setUser(newUser);
			this.recentUser = newUser;
		}
	}
	
	public void onUserChanged(User user, UserState state) {
		LOG.debug("onUserChanged(user="+user+", state="+state+")");
		
		if(this.recentUser == null && state == UserState.TRACKING) {
			this.window.setUser(user);
			this.recentUser = user;
			
		} else if(this.recentUser == user && state == UserState.LOST) {
			this.checkUsers();
		}
	}

}
