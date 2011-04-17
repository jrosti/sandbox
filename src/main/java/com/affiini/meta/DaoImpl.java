package com.affiini.meta;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

class DaoImpl implements Dao {
	@Inject EntityManager em; 
	
	@Transactional
	public CompositeKeyEntity create(String a, String b) {
		CompositeKeyEntity c = new CompositeKeyEntity(a,b, "some", BigInteger.valueOf(1000L));
		em.persist(c);
		return c;
	}
	
	@Override
	@Transactional
	public CompositeKeyEntity findByText(String a, String b) {
		return em.find(CompositeKeyEntity.class, new CompositeId(a,b));
	}
	
	@Override
	@Transactional
	public Object nativeQuery() {
		Query q = em.createNativeQuery("SELECT SUM(t.antCount) FROM CompositeKeyEntity t WHERE t.antCount > 1");
		return q.getSingleResult();
		
	}
	
}
