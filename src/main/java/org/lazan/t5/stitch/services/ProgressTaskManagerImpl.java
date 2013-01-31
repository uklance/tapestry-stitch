package org.lazan.t5.stitch.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.services.ParallelExecutor;
import org.lazan.t5.stitch.model.ProgressTask;

public class ProgressTaskManagerImpl implements ProgressTaskManager {
	private ParallelExecutor parallelExecutor;
	private ConcurrentMap<Integer, ProgressTask> executingTasks = new ConcurrentHashMap<Integer, ProgressTask>();
	private AtomicInteger nextId = new AtomicInteger(1);
	
	public ProgressTaskManagerImpl(ParallelExecutor parallelExecutor) {
		super();
		this.parallelExecutor = parallelExecutor;
	}

	public int add(final ProgressTask progressTask) {
		final int taskId = nextId.getAndIncrement();
		executingTasks.put(taskId, progressTask);
		Invokable<Void> invokable = new Invokable<Void>() {
			public Void invoke() {
				try {
					progressTask.run();
				} finally {
					executingTasks.remove(taskId);
				}
				return null;
			}
		};
		parallelExecutor.invoke(invokable);
		return taskId;
	}
	
	public float getProgress(int taskId) {
		ProgressTask progressTask = executingTasks.get(taskId);
		return progressTask == null ? 1f : progressTask.getProgress();
	}
}
