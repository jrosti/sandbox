package com.affiini.meta;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;



public class Application {
		  
	private Injector injector;

	private Application configure() {
	    injector = Guice.createInjector(new JpaPersistModule("testUnit"));
	    injector.getInstance(PersistService.class).start();
		return this;
	}
	
	private void run() {
		Dao service = injector.getInstance(Dao.class);
		service.create("foobar", "bax");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Application().configure().run();
	}

}
