package net.sf.ponyo.playground.jogl;

import javax.swing.SwingUtilities;

import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.core.ContextStarterImpl;
import net.sf.ponyo.jponyo.core.GlobalSpace;

@SuppressWarnings("synthetic-access")
public class App implements MainWindowListener {
	
	private static final long serialVersionUID = 7633042051769682994L;

	public static boolean isTracking_HACK = false;
	
	private final ContextStarter contextStarter;
	
	private Context context;
	private GlobalSpace data;
	private MainWindow window;
	
	public static void main(String[] args) {
		System.err.println("emtpy ;) as outcommented");
//		new App().startUp();
	}
	
	public App(ContextStarter contextStarter) {
		this.contextStarter = contextStarter;
	}



	public void startUp() {
//		this.context = new ContextStarterImpl().startOscReceiver();
		
//		this.context.addUserChangeListener(new UserChangeListener() {
//			public void onUserTracking(User user) {
//				data.isFooTracking = true;
//			}
//			public void onUserNew(User newUser) {
//			}
//			public void onUserLost(User user) {
//				data.isFooTracking = false;
//			}
//			public void onUserCalibrationStarted(User user) {
//			}
//			public void onUserCalibrationFailed(User user) {
//			}
//		});
		this.data = this.context.getGlobalSpace();
		
		this.window = new MainWindow(this.data, this);
		
	    this.window.setSize(800, 600);
	    SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				System.out.println("displaying window");
				App.this.window.display();
			}
		});
	}

	@Override
	public void onQuit() {
		this.window.stop();
		this.context.shutdown();
		this.window.setVisible(false);
//		this.window.dispose();
		System.exit(0);
	}
}
