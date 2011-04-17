package net.sf.ponyo.jponyo.adminconsole;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.adminconsole.view.MainWindow;
import net.sf.ponyo.jponyo.adminconsole.view.MainWindowListener;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.core.GlobalSpace;
import net.sf.ponyo.jponyo.user.User;
import net.sf.ponyo.jponyo.user.UserChangeListener;
import net.sf.ponyo.jponyo.user.UserState;

public class AdminConsoleApp implements MainWindowListener, UserChangeListener {

	public static boolean isTracking_HACK = false;
	
	private Context context;
	private GlobalSpace data;
	MainWindow window;
	private int userCount = 0;
	
	public static void main(String[] args) {
		System.out.println("main() START");
		new AdminConsoleApp().startUp();
		System.out.println("main() END");
	}

	public void startUp() {
		this.context = new ContextStarter().startOscReceiver();
		this.context.addUserChangeListener(this);
		this.data = this.context.getGlobalSpace();
		this.window = new MainWindow(this.data, this);
		
	    this.window.setSize(800, 600);
	    SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("displaying window");
				AdminConsoleApp.this.window.display();
			}
		});
	}

	public void onQuit() {
		this.window.destroy();
		this.context.shutdown();
		System.exit(0);
	}

	public void onUserChanged(User user, UserState state) {
		if(state == UserState.NEW) {
			this.userCount++;
		} else if(state == UserState.LOST) {
			this.userCount--;
		}
		AdminConsoleApp.isTracking_HACK = this.userCount != 0;
	}
}
