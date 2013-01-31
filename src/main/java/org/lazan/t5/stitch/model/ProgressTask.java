package org.lazan.t5.stitch.model;

public interface ProgressTask extends Runnable {
	/**
	 * Get the percentage complete of the task
	 * @return a decimal between 0 and 1
	 */
	public float getProgress();
}
