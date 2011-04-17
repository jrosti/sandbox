package com.affiini.meta;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class CompositeKeyTest {
	private Injector injector;

	@Before
	public void setUp() {
		injector = Guice.createInjector(new JpaPersistModule("testUnit"));	    
		injector.getInstance(PersistService.class).start();
	}

	@Test
	public void testCompositeCreate() {
		final Dao dao = injector.getInstance(Dao.class);
		dao.create("a", "b");
		CompositeKeyEntity c = dao.findByText("a", "b");
		Assert.assertEquals("a", c.getA());
		Object o = dao.nativeQuery(); 
		Assert.assertEquals(1000L, ((BigDecimal)o).longValue());
	}

	@After
	public final void tearDown() {
	}

}
