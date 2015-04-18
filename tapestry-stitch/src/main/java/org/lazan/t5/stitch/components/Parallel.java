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
		// temp element will be removed later
		final Element placeholder = writer.element("div");
		writer.end();
		
		ParallelModel model = new ParallelModel() {
			@Override
			public Invokable<?> getWorker() {
				return Parallel.this.worker;
			}

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
		
		// returning false causes stops the body block from rendering here
		return false;
	}


	private List<Node> renderBody() {
		Block bodyBlock = resources.getBody();
		RenderCommand renderCommand = typeCoercer.coerce(bodyBlock, RenderCommand.class);
		MarkupWriter writer = new MarkupWriterImpl();
		
		// this element is discarded
		Element root = writer.element("div");

		RenderQueueImpl queue = new RenderQueueImpl(logger);
		queue.push(renderCommand);
		queue.run(writer);
		writer.end();
		return root.getChildren();
	}
}
