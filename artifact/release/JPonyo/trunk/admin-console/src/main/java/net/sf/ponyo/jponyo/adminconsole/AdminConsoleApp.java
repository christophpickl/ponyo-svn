package net.sf.ponyo.jponyo.adminconsole;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.MainWindow;
import net.sf.ponyo.jponyo.adminconsole.view.MainWindowListener;
import net.sf.ponyo.jponyo.adminconsole.view.SkeletonNumberDialog;
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

public class AdminConsoleApp
	implements MainWindowListener, UserChangeListener, MotionStreamListener {

	private static final Log LOG = LogFactory.getLog(AdminConsoleApp.class);
	
	public static UserState userState_HACK = UserState.LOST;
	
	private Context context;
	private GlobalSpace space;
	private MainWindow window;
	private SkeletonNumberDialog skeletonDialog;
	
	public static void main(String[] args) {
		System.out.println("main() START");
		new AdminConsoleApp().startUp();
		System.out.println("main() END");
	}

	public void startUp() {
		this.context = new ContextStarter().startOscReceiver();
		this.space = this.context.getGlobalSpace();
		this.window = new MainWindow(this.space, this);
		this.skeletonDialog = new SkeletonNumberDialog();

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

	public void onUserChanged(User user, UserState state) {
		LOG.debug("onUserChanged(user="+user+", state="+state+")");
		AdminConsoleApp.userState_HACK = state;
	}

	public void onMotion(MotionData data) {
		this.skeletonDialog.update(data);
	}
}
