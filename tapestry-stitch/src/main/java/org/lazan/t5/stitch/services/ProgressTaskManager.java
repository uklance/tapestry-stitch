package org.lazan.t5.stitch.services;

import org.lazan.t5.stitch.model.ProgressTask;

public interface ProgressTaskManager {
	/**
	 * Add a task to be executed in parallel. This can be queried for it's progress
	 * 
	 * @return The taskId assigned to the task
	 */
	int submit(ProgressTask progressTask);

	/**
	 * Get the current progress of the task.
	 * Progress will be a value between 0 (no progress) and 1 (finished)
	 * 
	 * @param taskId The id of the task submitted by a previous call to add(ProgressTask)
	 * @return The progress value
	 */
	float getProgress(int taskId);
}
