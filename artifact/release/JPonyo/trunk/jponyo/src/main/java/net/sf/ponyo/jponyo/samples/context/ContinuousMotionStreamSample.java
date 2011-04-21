package net.sf.ponyo.jponyo.samples.context;

import net.sf.ponyo.jponyo.JPonyoModule;
import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class ContinuousMotionStreamSample {

	private final ContextStarter contextStarter;
	
	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(new JPonyoModule());
		ContinuousMotionStreamSample sample = injector.getInstance(ContinuousMotionStreamSample.class);
		sample.doit();
	}
	
	@Inject
	public ContinuousMotionStreamSample(ContextStarter contextStarter) {
		this.contextStarter = contextStarter;
	}

	public void doit() throws InterruptedException {
		final Context context = this.contextStarter.startOscReceiver();
		context.getContinuousMotionStream().addListener(new MotionStreamListener() {
			public void onMotion(MotionData data) {
				System.out.println("XXXXXXX onMotion: " + data);
			}
		});
		
		Thread.sleep(15 * 1000);
		context.shutdown();
		
	}
}
