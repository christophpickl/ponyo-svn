package net.sf.ponyo.playground.jogl;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import jponyo.GlobalData;
import jponyo.jna.PonyoNI;
import jponyo.jna.PonyoNIListener;
import jponyo.jna.UserStateConstant;

@SuppressWarnings("synthetic-access")
public class App implements PonyoNIListener, MainWindowListener {
	
	private static final long serialVersionUID = 7633042051769682994L;

	private final PonyoNI jna = new PonyoNI();
	private final GlobalData data = new GlobalData();
	private final MainWindow window = new MainWindow(this.data, this);
	private transient boolean jnaRunning = false;
	
	public static void main(String[] args) {
		new App().startUp();
	}
	
	public void startUp() {
	    this.window.setSize(800, 600);
	    SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				System.out.println("displaying window");
				App.this.window.display();
			}
		});
	    this.initJna();
	}

	private void initJna() {
	    this.jna.addListener(this);
		this.jna.initLib();
		new Thread(new Runnable() {
			@Override public void run() {
//				App.this.jna.startRecording("/myopenni/myoni.oni");
				App.this.jna.start();
			}
		}).start();
		this.jnaRunning = true;
	}

	@Override public void onJointPositionChanged(int userId, int joint, float x, float y, float z) {
		this.data.xByJoint[joint] = x;
		this.data.yByJoint[joint] = y;
		this.data.zByJoint[joint] = z;
		this.window.onJointUpdated();
	}

	@Override public void onUpdateThreadThrewException(String exceptionMessage) {
		JOptionPane.showMessageDialog(null, exceptionMessage, "Update Thread Aborted", JOptionPane.ERROR_MESSAGE);
		if(this.jnaRunning == true) {
			this.jna.shutdown();
			this.jnaRunning = false;
		}
	}

	@Override public void onUserStateChanged(int userId, int userState) {
		System.out.println("App.onUserStateChanged(userId="+userId+", userState="+userState+")");
		if(userState == UserStateConstant.CALIBRATION_ENDED_SUCCESSFULLY) {
			this.data.isTracking = true;
		} else if(userState == UserStateConstant.LOST_USER) {
			this.data.isTracking = false;
		}
	}

	@Override
	public void onQuit() {
		this.window.stop();
		if(this.jnaRunning == true) {
			this.jna.shutdown();
			this.jnaRunning = false;
		}
		this.window.setVisible(false);
//		this.window.dispose();
		System.exit(0);
	}
}
