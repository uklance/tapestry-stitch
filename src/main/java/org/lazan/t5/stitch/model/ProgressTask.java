package org.lazan.t5.stitch.model;

/**
 * A RrogressTask is a Runnable that can be queried periodically for
 * it's progress. Implementors must be aware that getProgress() will
 * polled asynchronously whilst the task is executing and may require
 * synchronization.
 */
public interface ProgressTask extends Runnable {
	/**
	 * Get the current progress of the task.
	 * @return a decimal between 0 (not started) and 1 (complete)
	 */
	public float getProgress();
}
