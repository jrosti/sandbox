package com.affiini.hazel;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Injector;

public class HazelCastServletConfigTest {

	@Test
	public void test() {
		Injector injector = new HazelCastServletConfig().getInjector();
		assertNotNull(injector.getInstance(HazelFilter.class));
	}

}
