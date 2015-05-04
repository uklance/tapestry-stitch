package org.lazan.t5.stitch.demo.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

public class ParallelDemo {
	public static final int pauseSeconds = 2;
	
	private int renderOrder;
	
	@Property
	private int workerCount;
	
	@Inject @Symbol("tapestry.thread-pool.core-pool-size")
	@Property 
	private int threadCount;
	
	@Property
	private String rowValue;

	@Property
	private String parallelRowValue;
	
	@Property
	private Integer rowIndex;
	
	private long startMillis;

	@SetupRender
	void setupRender() {
		renderOrder = 0;
		workerCount = 0;
		startMillis = System.currentTimeMillis();
	}
	
	public String getRenderTime() {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS (z)");
		return timeFormat.format(new Date());
	}
	
	/**
	 * Creates an invokable which pauses for a second before returning the rowValue
	 */
	public Invokable<String> createRowWorker() {
		final String valueSnapshot = rowValue;
		Invokable<String> worker = new Invokable<String>() {
			@Override
			public String invoke() {
				try {
					Thread.sleep(pauseSeconds * 1000);
				} catch (Exception e) {}
				
				return valueSnapshot;
			}
		};
		++ workerCount;
		return worker;
	}
	
	public boolean isEvenRow() {
		return rowIndex % 2 == 0;
	}
	
	public int getRenderOrder() {
		return ++renderOrder;
	}
	
	public double getRenderSeconds() {
		return (System.currentTimeMillis() - startMillis) / 1000D;
	}
	
	public int getTotalWorkSeconds() {
		return workerCount * pauseSeconds;
	}
}
