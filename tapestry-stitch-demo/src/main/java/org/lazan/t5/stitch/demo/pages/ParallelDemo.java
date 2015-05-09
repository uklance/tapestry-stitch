package org.lazan.t5.stitch.demo.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

public class ParallelDemo {
	public static final int pauseSeconds = 2;
	
	private int renderOrder;
	
	@Property
	private AtomicInteger workerCount;
	
	@Inject @Symbol("tapestry.thread-pool.core-pool-size")
	@Property 
	private int threadCount;
	
	@Property
	private String loopValue;

	@Property
	private String parallelValue;
	
	@Property
	private Integer rowIndex;
	
	private long startMillis;

	@SetupRender
	void setupRender() {
		renderOrder = 0;
		workerCount = new AtomicInteger(0);
		startMillis = System.currentTimeMillis();
	}
	
	public String getRenderTime() {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS (z)");
		return timeFormat.format(new Date());
	}
	
	/**
	 * Creates an invokable which pauses for 2 seconds before returning the value
	 */
	public Invokable<String> createRowWorker(final String value) {
		// take a copy of workerCount since it's bound to the request thread
		final AtomicInteger workerCountCopy = workerCount;
		Invokable<String> worker = new Invokable<String>() {
			@Override
			public String invoke() {
				try {
					Thread.sleep(pauseSeconds * 1000);
				} catch (Exception e) {}
				workerCountCopy.incrementAndGet();
				return value;
			}
		};
		return worker;
	}
	
	public boolean isEvenRow() {
		return rowIndex % 2 == 0;
	}
	
	public int getRenderOrder() {
		return ++renderOrder;
	}
	
	public double getElapsedSeconds() {
		return (System.currentTimeMillis() - startMillis) / 1000D;
	}
	
	public int getTotalWorkSeconds() {
		return workerCount.get() * pauseSeconds;
	}
}
