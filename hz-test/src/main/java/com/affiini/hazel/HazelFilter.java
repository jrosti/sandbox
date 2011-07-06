package com.affiini.hazel;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(HazelFilter.class);
	private final HazelcastInstance hazelcastInstance;
	
	private static int i = 0; 
	
	@Inject
	public HazelFilter(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		log.info("processing " + req.getScheme());
		IMap<String, String> arbmap = hazelcastInstance.getMap("somemap");
		arbmap.put("abc", "val" + (++i));
		chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
