package net.sf.ponyo.jponyo.samples.context;

import net.sf.ponyo.jponyo.core.Context;
import net.sf.ponyo.jponyo.core.ContextStarter;
import net.sf.ponyo.jponyo.stream.MotionData;
import net.sf.ponyo.jponyo.stream.MotionStreamListener;

public class ContinuousMotionStreamSample {
	
	public static void main(String[] args) throws Exception {
		
		Context context = new ContextStarter().startOscReceiver();
		context.getContinuousMotionStream().addListener(new MotionStreamListener() {
			public void onMotion(MotionData data) {
				System.out.println("XXXXXXX onMotion: " + data);
			}
		});
		
		Thread.sleep(15 * 1000);
		context.shutdown();
	}
	
}
