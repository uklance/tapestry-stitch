package org.lazan.t5.stitch.model;

import org.apache.tapestry5.ioc.Invokable;

public interface ParallelModel {
	Invokable<?> getWorker();
	void onWorkerValue(Object value);
}
