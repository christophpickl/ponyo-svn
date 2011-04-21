package net.sf.ponyo.jponyo.samples;

import net.sf.ponyo.jponyo.connection.jna.JnaByConfigConnectorFactory;
import net.sf.ponyo.jponyo.connection.jna.JnaModule;
import net.sf.ponyo.jponyo.connection.osc.OscConnector;
import net.sf.ponyo.jponyo.connection.osc.OscConnectorFactory;
import net.sf.ponyo.jponyo.connection.osc.OscModule;
import net.sf.ponyo.jponyo.user.UserModule;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class SomeSample {

	public static void main(String[] args) {
		System.out.println("main() START");
		Injector injector = Guice.createInjector(new SampleJPonyoModule());
		
		injector.getInstance(Sample.class);
		
		System.out.println("main() END");
	}
	
	public static class Sample {
		@Inject
		public Sample(
				JnaByConfigConnectorFactory jnaConfigFactory,
				OscConnectorFactory oscFactory) {
//			jnaConfigFactory.create("config.xml").openConnection();
//			oscFactory.create(OscConnector.DEFAULT_PORT).openConnection();
		}
//		@Inject
//		public Sample(RunningSessionUserManagerFactory factory) {
//			factory.create(new UserManagerCallback() {
//				public void processUserStateChange(User user, UserState state) {
//					System.out.println("foobar");
//				}
//			});
//		}
	}
}
