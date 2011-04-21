package net.sf.ponyo.jponyo.connection.jna;

import net.sf.ponyo.jponyo.connection.Connector;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class JnaModule extends AbstractModule {

	@Override
	protected void configure() {

//		bind(Connector.class)
//			.annotatedWith(JnaByConfig.class)
//			.to(JnaByConfigConnector.class);

//		bind(Connector.class)
//			.annotatedWith(JnaByRecording.class)
//			.to(JnaByRecordingConnector.class);
		
		install(new FactoryModuleBuilder()
			.implement(Connector.class, JnaByConfigConnectorImpl.class)
			.build(JnaByConfigConnectorFactory.class));
		
		install(new FactoryModuleBuilder()
			.implement(Connector.class, JnaByRecordingConnectorImpl.class)
			.build(JnaByRecordingConnectorFactory.class));
	}

}
