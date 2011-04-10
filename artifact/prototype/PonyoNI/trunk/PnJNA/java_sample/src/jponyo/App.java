package jponyo;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import jponyo.gui.MainWindow;
import jponyo.gui.MainWindow.MainWindowListener;
import jponyo.jna.PonyoNI;
import jponyo.jna.PonyoNIListener;

public class App implements PonyoNIListener, MainWindowListener {
	
	private final PonyoNI jna = new PonyoNI();
	private final GlobalData data = new GlobalData();
	final MainWindow window = new MainWindow(this);
	
	private transient boolean jnaIsStarted = false; 
	
	public static void main(String[] args) {
    	new App().start();
	}
	
	public void start() {
		System.out.println("App.start() START");
		this.initGui();
		this.initJna();
		
		System.out.println("App.start() END");
	}
	
	private void initJna() {
		System.out.println("App.iniJna()");
		this.jna.addListener(this);
		this.jna.initLib();
	}
	
	public void onQuit() {
		System.out.println("App.onQuit() START");
		if(this.jnaIsStarted == true) {
			this.jna.shutdown();
			this.jnaIsStarted = false;
		}
		
		System.out.println("Hiding and disposing window");
		this.window.setVisible(false);
		this.window.dispose();
		System.out.println("App.onQuit() END");
	}
	
	private void initGui() {
		System.out.println("App.initGui()");
		SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
				System.out.println("App.SwingUtilities.run() ... displaying window");
				App.this.window.setVisible(true);
			} }); }
	
	@Override public void onUserStateChanged(int userId, int userState) {
		System.out.println("App.onUserStateChanged(userId="+userId+", userState="+userState+")");
	}

	@Override public void onJointPositionChanged(int userId, int joint, float x, float y, float z) {
//		System.out.println("onJointPositionChanged(userId="+userId+", joint="+joint+", x="+x+", y="+y+", z="+z+")");
		this.data.xByJoint[joint] = x;
		this.data.yByJoint[joint] = y;
		this.data.zByJoint[joint] = z;
		this.window.update(this.data);
	}

	@Override public void onUpdateThreadThrewException(String exceptionMessage) {
		System.out.println("App.onUpdateThreadThrewException(exceptionMessage=[" + exceptionMessage + "])");
		JOptionPane.showMessageDialog(null, exceptionMessage, "Update Thread Aborted", JOptionPane.ERROR_MESSAGE);
		System.out.println("Shutting down jna context ...");
		this.jna.shutdown();
	}

	@Override @SuppressWarnings("synthetic-access")
	public void onStart() {
		this.window.setJnaRunning(true);
		this.jnaIsStarted = true;
		
		new Thread(new Runnable() {
			@Override public void run() {
				App.this.jna.startRecording("/myopenni/myoni.oni");
//				App.this.jna.start();
			}
		}).start();
	}
}
