package org.lazan.t5.stitch.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.lazan.t5.stitch.model.MarkupHandler;

/**
 * This component captures it's body as it renders and removes it from the DOM.
 * It then updates it's 'handler' and / or 'value' parameter with the markup
 */
public class Capture {
	@Parameter
	private MarkupHandler handler;
	
	@Parameter
	private String value;
	
	@Inject
	private ComponentResources resources;	
	
	@BeforeRenderBody
	void beforeRenderBody(MarkupWriter writer) {
		if (handler == null && !resources.isBound("value")) {
			throw new IllegalStateException("A 'handler' or a 'value' must be specified");
		}
		
		// add a temp container to the DOM (this will be removed later)
		writer.element("container");
	}
	
	@AfterRenderBody
	void afterRenderBody(MarkupWriter writer) {
		Element container = writer.getElement();
		writer.end();
		String markup = container.getChildMarkup();
		
		// remove the temp container
		container.remove();
		
		if (handler != null) {
			handler.handle(markup);
		}
		if (resources.isBound("value")) {
			value = markup;
		}
	}
}
