package org.lazan.t5.stitch.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
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
	private Request request;
	
	private boolean renderBody;
	
	@BeforeRenderBody
	boolean beforeRenderBody(MarkupWriter writer) {
		TabModel tabModel = (TabModel) request.getAttribute(TabGroup.ATTRIBUTE_TAB_MODEL);
		if (tabModel == null) {
			throw new IllegalStateException("Tab must be nested inside a TabGroup");
		}
		int tabIndex = tabModel.addLabel(label);
		
		renderBody = (tabIndex == tabModel.getActiveTabIndex());
		if (renderBody) {
			// add a container for the body
			writer.element("div");
		}	
		return renderBody;
	}
	
	@AfterRenderBody
	void afterRender(MarkupWriter writer) {
		if (renderBody) {
			// capture the body markup and remove it from the DOM, it will be rendered by the TabGroup
			Element bodyWrapper = writer.getElement();
			writer.end();
			TabModel tabModel = (TabModel) request.getAttribute(TabGroup.ATTRIBUTE_TAB_MODEL);
			tabModel.setActiveTabBody(bodyWrapper.getChildMarkup());
			bodyWrapper.remove();
		}
	}
}