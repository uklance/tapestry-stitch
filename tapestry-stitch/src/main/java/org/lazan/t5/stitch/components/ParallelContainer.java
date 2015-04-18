package org.lazan.t5.stitch.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ParallelExecutor;
import org.apache.tapestry5.services.Environment;
import org.lazan.t5.stitch.model.ParallelContainerModel;
import org.lazan.t5.stitch.model.ParallelModel;

public class ParallelContainer {
	@Inject
	private ParallelExecutor executor;
	
	@Inject
	private Environment environment;
	
	private List<ParallelModel> parallelModels;

	@SetupRender
	void setupRender() {
		parallelModels = new ArrayList<ParallelModel>();
		
		ParallelContainerModel containerModel = new ParallelContainerModel() {
			@Override
			public void add(ParallelModel parallelModel) {
				parallelModels.add(parallelModel);
			}
		};
		
		environment.push(ParallelContainerModel.class, containerModel);
	}
	
	@AfterRenderBody
	void afterRenderBody(MarkupWriter writer) throws Exception {
		environment.pop(ParallelContainerModel.class);
		
		if (parallelModels.isEmpty()) {
			return;
		}
		
		List<Future<?>> futures = new ArrayList<Future<?>>();
		for (ParallelModel parallelModel : parallelModels) {
			futures.add(executor.invoke(parallelModel.getWorker()));
		}
		Iterator<Future<?>> it = futures.iterator();
		for (ParallelModel parallelModel : parallelModels) {
			Future<?> future = it.next();
			Object workerValue = future.get();
			parallelModel.onWorkerValue(workerValue);
		}
	}
}
