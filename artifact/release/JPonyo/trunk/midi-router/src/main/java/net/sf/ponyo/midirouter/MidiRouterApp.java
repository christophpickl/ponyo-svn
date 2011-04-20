package net.sf.ponyo.midirouter;

import net.sf.ponyo.midirouter.presenter.MainPresenter;
import net.sf.ponyo.midirouter.presenter.MainPresenterListener;
import net.sf.ponyo.midirouter.view.MainView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MidiRouterApp implements MainPresenterListener {
	
	private static final Log LOG = LogFactory.getLog(MidiRouterApp.class);
	
	private MainPresenter presenter;
	
	public static void main(String[] args) {
		new MidiRouterApp().startApplication();
	}
	
	public void startApplication() {
		LOG.info("startApplication()");
		final MainView window =  new MainView();
		this.presenter = new MainPresenter(window);
		this.presenter.addListener(this);
		this.presenter.show();
	}

	public void onQuit() {
		LOG.debug("onQuit()");
		// TODO destroy connection, etc
	}

}
