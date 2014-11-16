package org.lazan.t5.stitch.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.lazan.t5.stitch.model.TabGroupModel;

/**
 * NB: I would have preferred to use an Environmental instead of a request attribute but I can't
 * http://tapestry.1045711.n5.nabble.com/5-4-alpha-2-Environment-cloaked-during-ajax-component-event-td5719496.html
 */
public class Tab {
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String name;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value="prop:name")
	private String label;

	@Inject 
	private Request request;
	
	@Inject
	private ComponentResources resources;
	
	@BeforeRenderBody
	boolean beforeRenderBody(MarkupWriter writer) {
		TabGroupModel tabModel = (TabGroupModel) request.getAttribute(TabGroup.ATTRIBUTE_MODEL);
		if (tabModel == null) {
			throw new IllegalStateException("Tab must be nested inside a TabGroup");
		}
		tabModel.addTab(name, label, resources.getBody());
		
		// don't render the body, it will be rendered by the TabGroup
		return false;
	}
}