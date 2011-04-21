package net.sf.ponyo.jponyo.connection.osc;

import net.sf.ponyo.jponyo.connection.Connector;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class OscModule extends AbstractModule {

	@Override
	protected void configure() {
		
//		bind(Connector.class)
//			.annotatedWith(Osc.class)
//			.to(OscConnector.class);
		
		install(new FactoryModuleBuilder()
			.implement(Connector.class, OscConnector.class)
			.build(OscConnectorFactory.class));
		
	}

}
