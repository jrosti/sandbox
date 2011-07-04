package com.affiini.hazel;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class HazelCastServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(
				new AbstractModule() {

					@Override
					protected void configure() {
						bind(HazelFilter.class).in(Singleton.class);
					}
				},
				
				new ServletModule() {
					@Override
					protected void configureServlets() {
						filter("/*").through(HazelFilter.class);

					}			
		});
	}
}
