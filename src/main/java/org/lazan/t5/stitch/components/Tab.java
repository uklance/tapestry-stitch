package org.lazan.t5.stitch.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.lazan.t5.stitch.model.TabModel;

/**
 * NB: I would have preferred to use an Environmental instead of a request attribute but I can't
 * http://tapestry.1045711.n5.nabble.com/5-4-alpha-2-Environment-cloaked-during-ajax-component-event-td5719496.html
 */
public class Tab {
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String label;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject 
	private Request request;
	
	@SetupRender
	boolean setupRender() {
		TabModel tabModel = (TabModel) request.getAttribute(TabGroup.ATTRIBUTE_TAB_MODEL);
		if (tabModel == null) {
			throw new IllegalStateException("Tab must be nested inside a TabGroup");
		}
		tabModel.getLabelsById().put(componentResources.getId(), label);
		
		// the active tab's body will be rendered by the TabGroup
		return false;
	}
}