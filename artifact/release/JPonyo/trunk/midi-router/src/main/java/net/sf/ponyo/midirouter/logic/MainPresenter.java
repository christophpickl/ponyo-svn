package net.sf.ponyo.midirouter.logic;

import net.sf.ponyo.jponyo.common.async.Async;

public interface MainPresenter extends Async<MainPresenterListener> {

	void show();

}