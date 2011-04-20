package net.sf.ponyo.midirouter.refactor.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.pulseproject.commons.util.GuiUtil;
import net.sf.ponyo.jponyo.common.gui.OSXAdapter;
import net.sf.ponyo.midirouter.refactor.Model;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = -2564259433681771255L;
	public static final Font FONT = new Font("Monaco", Font.PLAIN, 11);
	private final MainWindowListener listener;
	
	private final Model model;
	private final MainPanel mainPanel;
	public MainWindow(Model model, MainWindowListener listener, final String applicationVersion) {
		super("MidiRoute Prototype v" + applicationVersion);
		this.listener = listener;
		this.model = model;
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				onWindowClosing();
			}
		});

		this.mainPanel = new MainPanel(model, this.listener);
		this.getRootPane().setDefaultButton(this.mainPanel.getDefaultButton());
		this.getContentPane().add(this.mainPanel);
		

		final int recentX = model.recentWindowX;
		final int recentY = model.recentWindowY;
		if(recentX == -1 || recentY == -1) {
			GuiUtil.setCenterLocation(this);
		} else {
			this.setLocation(recentX, recentY);
		}
		

		final int recentWidth = model.recentWindowWidth;
		final int recentHeight = model.recentWindowHeight;
		if(recentWidth == -1 || recentHeight == -1) {
			this.pack();
		} else {
			this.setSize(recentWidth, recentHeight);
		}
		
		if(IS_MACOSX) {
			this.registerOsxListener();
		}
	}

	private static final boolean IS_MACOSX = System.getProperty("os.name")
		.toLowerCase(Locale.ENGLISH).startsWith("mac os x");
	private void registerOsxListener() {
		try {
			OSXAdapter.setQuitHandler(this, this.getClass().getDeclaredMethod("osxQuit", (Class[]) null));
			OSXAdapter.setPreferencesHandler(this,
					this.getClass().getDeclaredMethod("osxPreferences", (Class[]) null));
		} catch (final SecurityException e) {
			throw new RuntimeException("Could not initialize Mac OS X handlers!", e);
		} catch (final NoSuchMethodException e) {
			throw new RuntimeException("Could not initialize Mac OS X handlers!", e);
		}
	}
	
	public final void osxQuit() {
		this.tearDown();
	}

	public final void osxPreferences() {
//		this.onPreferences();
		Toolkit.getDefaultToolkit().beep();
	}
	
	void onWindowClosing() {
		this.tearDown();
	}
	
	private void tearDown() {
		this.mainPanel.tearDown();
		final Point location = this.getLocation();
		this.model.recentWindowX = location.x;
		this.model.recentWindowY = location.y;
		
		final Dimension size = this.getSize();
		this.model.recentWindowWidth = size.width;
		this.model.recentWindowHeight = size.height;
		
		this.listener.onQuit();
		this.dispose();
	}

}
