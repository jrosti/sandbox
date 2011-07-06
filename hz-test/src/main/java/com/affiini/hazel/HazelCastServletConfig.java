package com.affiini.hazel;

import java.io.File;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.hazelcast.config.Config;

public class HazelCastServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		Config config = new Config();
		config.setConfigurationFile(new File("hazelcast.xml"));
		return Guice.createInjector(
				new HazelModule(config),
				new ServletModule() {
					@Override
					protected void configureServlets() {
						filter("/*").through(HazelFilter.class);
					}			
		});
	}
}
