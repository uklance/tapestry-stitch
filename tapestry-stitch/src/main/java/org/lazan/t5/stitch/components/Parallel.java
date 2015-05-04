package org.lazan.t5.stitch.components;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.internal.services.MarkupWriterImpl;
import org.apache.tapestry5.internal.services.RenderQueueImpl;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.runtime.RenderCommand;
import org.lazan.t5.stitch.model.ParallelContainerModel;
import org.lazan.t5.stitch.model.ParallelModel;
import org.slf4j.Logger;

/**
 * Works in concert with the {@link ParallelContainer} to perform work in parallel. The rendering
 * of the body is delayed until the {@link ParallelContainer} finishes rendering.
 */
public class Parallel {
	@Parameter(required=true)
	private Object binding;
	
	@Parameter(required=true)
	private Invokable<?> worker;
	
	@Environmental
	private ParallelContainerModel containerModel;
	
	@Inject
	private ComponentResources resources;
	
	@Inject
	private TypeCoercer typeCoercer;
	
	@Inject
	private Logger logger;
	
	@SetupRender
	public boolean setupRender(MarkupWriter writer) {
		// insert a placeholder in the DOM (will be removed later)
		final Element placeholder = writer.element("div");
		writer.end();
		
		// take a snapshot of the worker value for cases where 
		// the parallel instance is re-used (eg in a loop)
		final Invokable<?> workerSnapshot = worker;
		
		ParallelModel model = new ParallelModel() {
			@Override
			public Invokable<?> getWorker() {
				return workerSnapshot;
			}

			/**
			 * Fired by the parallelContainer
			 */
			@Override
			public void onWorkerValue(Object value) {
				Parallel.this.binding = value;
				
				List<Node> nodes = renderBody();
				
				for (Node node : nodes) {
					node.moveBefore(placeholder);
				}
				
				placeholder.remove();
			}
		};
		
		containerModel.add(model);
		
		// returning false stops the body from rendering here
		return false;
	}


	private List<Node> renderBody() {
		Block bodyBlock = resources.getBody();
		RenderCommand renderCommand = typeCoercer.coerce(bodyBlock, RenderCommand.class);
		MarkupWriter writer = new MarkupWriterImpl();
		
		// root element will be discarded
		Element root = writer.element("div");

		RenderQueueImpl queue = new RenderQueueImpl(logger);
		queue.push(renderCommand);
		queue.run(writer);
		writer.end();
		return root.getChildren();
	}
}
