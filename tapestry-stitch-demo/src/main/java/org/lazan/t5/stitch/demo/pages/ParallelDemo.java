package org.lazan.t5.stitch.demo.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

public class ParallelDemo {
	static final long pauseSeconds = 1;
	
	private long startMillis;
	
	@Inject @Symbol("tapestry.thread-pool.max-pool-size")
	@Property 
	private int maxThreadPoolSize;
	
	@Property
	private String property1;

	@Property
	private String property2;

	@Property
	private String property3;
	
	@SetupRender
	void setupRender() {
		startMillis = System.currentTimeMillis();
	}
	
	public double getRenderSeconds() {
		return (System.currentTimeMillis() - startMillis) / 1000D;
	}
	
	public Invokable<String> getWorker1() {
		return createPauseWorker("value1");
	}

	public Invokable<String> getWorker2() {
		return createPauseWorker("value2");
	}

	public Invokable<String> getWorker3() {
		return createPauseWorker("value3");
	}

	/**
	 * Creates an invokable which pauses before returning the value
	 */
	private Invokable<String> createPauseWorker(final String value) {
		return new Invokable<String>() {
			@Override
			public String invoke() {
				try {
					Thread.sleep(pauseSeconds * 1000);
				} catch (Exception e) {}
				return value;
			}
		};
	}
}
