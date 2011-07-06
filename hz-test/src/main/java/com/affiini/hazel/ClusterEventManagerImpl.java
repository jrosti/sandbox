package com.affiini.hazel;

import com.hazelcast.core.HazelcastInstance;

public class ClusterEventManagerImpl<T> implements ClusterEventManager<T> {

	private final HazelcastInstance hazelcastInstance;

	public ClusterEventManagerImpl(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}
	
	@Override
	public void publish(T event) {
	}

}
