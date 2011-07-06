package com.affiini.hazel;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

final class HazelModule extends AbstractModule {
	
	private final Config config; 
	
	public HazelModule(Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		bind(HazelFilter.class).in(Singleton.class);
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
		
		ClusterEventManager<ClusterEvent> clusterEventManager = new ClusterEventManagerImpl<ClusterEvent>(hazelcastInstance);
		bind(new TypeLiteral<ClusterEventManager<ClusterEvent>>() {}).toInstance(clusterEventManager);
		bind(HazelcastInstance.class).toInstance(hazelcastInstance);
	}
}