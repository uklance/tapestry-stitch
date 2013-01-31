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

	/**
	 * Add a task to be executed in parallel. This can be queried for it's progress
	 * 
	 * @return The taskId assigned to the task
	 */
	public int submit(final ProgressTask progressTask) {
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
	
	/**
	 * Get the current progress of the task.
	 * Progress will be a value between 0 (no progress) and 1 (finished)
	 * 
	 * @param taskId The id of the task submitted by a previous call to add(ProgressTask)
	 * @return The progress value
	 */
	public float getProgress(int taskId) {
		ProgressTask progressTask = executingTasks.get(taskId);
		return progressTask == null ? 1f : progressTask.getProgress();
	}
}
