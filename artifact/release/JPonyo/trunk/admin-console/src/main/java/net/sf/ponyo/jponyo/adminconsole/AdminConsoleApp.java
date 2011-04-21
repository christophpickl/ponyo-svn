package net.sf.ponyo.jponyo.adminconsole;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.AdminPanelListener;
import net.sf.ponyo.jponyo.adminconsole.view.SkeletonDataDialog;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.core.ContextStarterImpl;
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

public class AdminConsoleApp
	implements AdminPanelListener, UserChangeListener, MotionStreamListener {

	private static final Log LOG = LogFactory.getLog(AdminConsoleApp.class);
	
	private final ContextStarter contextStarter;
	private Context context;
	private GlobalSpace space;
	private AdminConsoleWindow window;
	private SkeletonDataDialog skeletonDialog;
	private User recentUser;
	
	public static void main(String[] args) {
		System.out.println("main() START");
		Injector injector = Guice.createInjector(new AdminConsoleModule());
		AdminConsoleApp app = injector.getInstance(AdminConsoleApp.class);
		app.startUp();
		System.out.println("main() END");
	}

	@Inject
	public AdminConsoleApp(ContextStarter contextStarter) {
		this.contextStarter = contextStarter;
	}

	public void startUp() {
		this.context = this.contextStarter.startOscReceiver();
		this.space = this.context.getGlobalSpace();
		this.window = new AdminConsoleWindow(this);
		this.skeletonDialog = new SkeletonDataDialog();

		this.checkUsers();
		this.context.addUserChangeListener(this);
		
		this.context.getContinuousMotionStream().addListener(this);
		
	    this.window.setSize(800, 600);
	    SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AdminConsoleApp.this.onShowWindow();
			}
		});
	}
	
	void onShowWindow() {
		System.out.println("displaying window");
		
		this.window.display();
		Dimension windowSize = this.window.getSize();
		Point windowLocation = this.window.getLocation();
		this.skeletonDialog.setLocation(windowLocation.x + windowSize.width + 4, windowLocation.y);
		this.skeletonDialog.setVisible(true);
	}

	public void onQuit() {
		this.skeletonDialog.dispose();
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

	public void onMotion(MotionData data) {
		this.skeletonDialog.update(data);
	}
}
