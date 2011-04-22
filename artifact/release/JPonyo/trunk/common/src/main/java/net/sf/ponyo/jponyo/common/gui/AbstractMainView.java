package net.sf.ponyo.jponyo.common.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.sf.ponyo.jponyo.common.async.Async;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.common.binding.BindingProvider;
import net.sourceforge.jpotpourri.jpotface.panel.brushed.PtBrushedMetalPanel;
import net.sourceforge.jpotpourri.tools.PtUserSniffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractMainView<L extends AbstractMainViewListener, P extends BindingProvider>
	extends JFrame
		implements Async<L> {

	private static final long serialVersionUID = -1714573026004141885L;
	private static final Log LOG = LogFactory.getLog(AbstractMainView.class);
	
	private final P provider;
	private final DefaultAsync<L> async = new DefaultAsync<L>();
	private boolean isInitialized = false;

	private final PtBrushedMetalPanel mainPanel = new PtBrushedMetalPanel();
	
	public AbstractMainView(P provider, String windowTitle) {
		this.provider = provider;
		this.setTitle(windowTitle);
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onWindowClosing();
			}
            @Override
			public void windowActivated(WindowEvent event) {
            	windowDidActivate(true);
            }
            @Override
			public void windowDeactivated(WindowEvent event) {
            	windowDidActivate(false);
            }
		});
	}
    
	void windowDidActivate(boolean didActivate) {
//    	LOG.trace("windowDidActivate(" + didActivate + ")");
    	
//    	this.activated = didActivate;
    	this.mainPanel.setActive(didActivate);
    	this.repaint();
    }
	
	protected abstract Component initComponent(P sameProvider);

	public final void display() {
		this.display(null);
	}
	
	public final void display(Dimension enforcedSize) {
		this.display(enforcedSize, null);
	}
	
	public final void display(Dimension enforcedSize, Point offset) {
		if(this.isInitialized == false) {
			this.initialize(enforcedSize, offset);
		}
		
		this.setVisible(true);
	}
	
	private void initialize(Dimension enforcedSize, Point offset) {
		LOG.debug("initialize()");
		
		this.mainPanel.setLayout(new BorderLayout());
		Component innerContent = this.initComponent(this.provider);
		if(innerContent == null) {
			throw new RuntimeException("initComponent() may not return null by: " + this.getClass().getName());
		}
		this.mainPanel.add(innerContent, BorderLayout.CENTER);
		this.getContentPane().add(this.mainPanel);
		
		if(enforcedSize == null) {
			this.pack();
		} else {
			this.setSize(enforcedSize);
		}
		
		if(offset == null) {
			GuiUtil.setCenterLocation(this);
		} else {
			GuiUtil.setCenterLocation(this, offset.x, offset.y);
		}
		
		if(PtUserSniffer.isMacOSX()) {
			this.registerOsxListener();
		}
		
		this.isInitialized = true;
	}
	
	private void registerOsxListener() {
		LOG.debug("registerOsxListener()");
		try {
			OSXAdapter.setQuitHandler(this, AbstractMainView.class.getDeclaredMethod("onOsxQuit", (Class[]) null));
			OSXAdapter.setPreferencesHandler(this, AbstractMainView.class.getDeclaredMethod("onOsxPreferences", (Class[]) null));
		} catch (final SecurityException e) {
			throw new RuntimeException("Could not initialize Mac OS X handlers!", e);
		} catch (final NoSuchMethodException e) {
			throw new RuntimeException("Could not initialize Mac OS X handlers!", e);
		}
	}

	public final void onOsxQuit() {
		LOG.debug("onOsxQuit()");
		this.dispatchQuit();
	}
	
	public final void onOsxPreferences() {
		LOG.debug("onOsxPreferences()");
		Toolkit.getDefaultToolkit().beep(); // FIXME implement preferences window
	}
	
	void onWindowClosing() {
		LOG.debug("onWindowClosing()");
		this.dispatchQuit();
	}
	
	protected final void dispatchQuit() {
		for (L listener : this.async.getListeners()) {
			listener.onQuit();
		}
	}
	public final void addListener(L listener) {
		this.async.addListener(listener);
	}

	public final void removeListener(L listener) {
		this.async.removeListener(listener);
	}
	
	protected final Iterable<L> getListeners() {
		return this.async.getListeners();
	}
}
