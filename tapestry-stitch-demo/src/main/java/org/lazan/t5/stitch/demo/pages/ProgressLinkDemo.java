package org.lazan.t5.stitch.demo.pages;

import org.lazan.t5.stitch.model.ProgressTask;

public class ProgressLinkDemo {
	public ProgressTask getTask() {
		return new TestTask();
	}
	
	public static class TestTask implements ProgressTask {
		public static final int RUN_MILLIS = 10 * 1000;
		public static final int SLEEP_MILLIS = 100;
		
		private float progress = 0;
		
		public void run() {
			int loopCount = RUN_MILLIS / SLEEP_MILLIS;
			for (int i = 0; i < loopCount; ++ i) {
				try {
					Thread.sleep(SLEEP_MILLIS);
				} catch (InterruptedException e) {}
				progress = i * 1f / loopCount;
			}
		}
		
		public float getProgress() {
			return progress;
		}
	}
}
