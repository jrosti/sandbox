package com.affiini.meta;

import com.google.inject.AbstractModule;

public class FactorialModule extends AbstractModule {

	public static class Evaluate {
		public int value() {
			return 5;
		}
	}
	
	@Override
	protected void configure() {
		
	}

	
	
	
}
