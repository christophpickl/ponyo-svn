package net.sf.ponyo.midirouter.view.framework;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.sf.ponyo.jponyo.common.async.Async;
import net.sf.ponyo.jponyo.common.async.DefaultAsync;
import net.sf.ponyo.jponyo.common.binding.BindingProvider;
import net.sf.ponyo.jponyo.common.gui.OSXAdapter;
import net.sourceforge.jpotpourri.tools.PtUserSniffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractMainView<L extends AbstractMainViewListener, P extends BindingProvider> extends JFrame implements Async<L> {

	private static final long serialVersionUID = -1714573026004141885L;
	private static final Log LOG = LogFactory.getLog(AbstractMainView.class);
	
	private final P provider;
	private final DefaultAsync<L> async = new DefaultAsync<L>();
	private boolean isInitialized = false;
	
	public AbstractMainView(P provider, String windowTitle) {
		this.provider = provider;
		this.setTitle(windowTitle);
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onWindowClosing();
			}
//			public void windowDeactivated(WindowEvent e) {
//			public void windowActivated(WindowEvent arg0) {
		});
	}
	
	protected abstract Component initComponent(P provider);

	public void display() {
		this.display(null);
	}
	
	public void display(Dimension enforcedSize) {
		if(this.isInitialized == false) {
			this.initialize(enforcedSize);
		}
		
		this.setVisible(true);
	}
	
	private void initialize(Dimension enforcedSize) {
		LOG.debug("initialize()");
		this.getContentPane().add(this.initComponent(this.provider));
		
		if(enforcedSize == null) {
			this.pack();
		} else {
			this.setSize(enforcedSize);
		}
		
		this.moveToCenterLocation();
		
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
	
	private void moveToCenterLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
	}
	
	void onWindowClosing() {
		LOG.debug("onWindowClosing()");
		this.dispatchQuit();
	}
	
	private void dispatchQuit() {
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
