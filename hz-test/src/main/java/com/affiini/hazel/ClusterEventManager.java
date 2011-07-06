package com.affiini.hazel;

public interface ClusterEventManager<T> {
	
	void publish(T event);
	
}
