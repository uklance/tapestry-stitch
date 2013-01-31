package org.lazan.t5.stitch.services;

import org.lazan.t5.stitch.model.ProgressTask;

public interface ProgressTaskManager {
	float getProgress(int taskId);
	int add(ProgressTask progressTask);
}
