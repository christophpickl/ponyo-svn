package net.sf.ponyo.console.app;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.SwingUtilities;

import net.sf.ponyo.console.gl.GLUtil;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class ConsoleApp implements ConsoleWindowListener {

	private final Model model;
	private final ContextStarter contextStarter;
	private Context context;
	private ConsoleWindow window;

	static {
		GLUtil.checkJoglLibs();
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ConsoleModule());
		ConsoleApp app = injector.getInstance(ConsoleApp.class);
		app.startUp();
	}

	@Inject
	public ConsoleApp(Model model, ContextStarter contextStarter) {
		this.model = model;
		this.contextStarter = contextStarter;
	}

	public void startUp() {
//		System.err.println("startUp OUTCOMMENTED!!!!!!!!!");
		
		this.context = this.contextStarter.startOscReceiver();
//		this.space = this.context.getGlobalSpace();
		this.window = new ConsoleWindow(this.model);
		this.window.addListener(this);

		this.window.onCurrentUserChanged(this.context.getContinuousUserProvider().getCurrentUser()); // initialize user
		this.context.getContinuousMotionStream().addListener(this.window);
		this.context.getContinuousUserProvider().addListener(this.window);
		
	    SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ConsoleApp.this.onShowWindow();
			}
		});
	}
	
	void onShowWindow() {
		Point offset = new Point(-50, 0); // move a little bit to left
		this.window.display(new Dimension(800, 600), offset);
	}

	public void onQuit() {
		this.window.destroy();

		this.context.getContinuousMotionStream().removeListener(this.window);
		this.context.getContinuousUserProvider().removeListener(this.window);
		this.context.shutdown();
		
		System.exit(0);
	}
	

}
