package org.lazan.t5.stitch.components;

import java.util.Map;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.Request;
import org.lazan.t5.stitch.model.TabModel;

/**
 * NB: I would have preferred to use an Environmental instead of a request attribute but I can't
 * http://tapestry.1045711.n5.nabble.com/5-4-alpha-2-Environment-cloaked-during-ajax-component-event-td5719496.html
 */
public class TabGroup {
	public static final String ATTRIBUTE_TAB_MODEL = TabGroup.class.getName() + ".TAB_MODEL";
	
	@Inject
	private Request request;
	
	@Property
	private TabModel tabModel;
	
	@Property
	private Map.Entry<String, String> tabEntry;
	
	@Parameter
	private String active;
	
	@InjectComponent
	private Zone tabsZone;
	
	@SetupRender
	void setupRender() {
		setup();
	}
	
	@Inject
	private ComponentResources componentResources;
	
	void setup() {
		tabModel = new TabModel();
		request.setAttribute(ATTRIBUTE_TAB_MODEL, tabModel);
	}
	
	@CleanupRender
	void cleanupRender() {
		request.setAttribute(ATTRIBUTE_TAB_MODEL, null);
	}
	
	Block onTabChange(String tabId) {
		active = tabId;
		setup();
		return request.isXHR() ? tabsZone.getBody() : null;
	}
	
	public String getTabClass() {
		String id = active == null ? tabModel.getFirstId() : active;
		return tabEntry.getKey().equals(id) ? "active" : null;
	}
	
	public Block getActiveTabBody() {
		String id = active == null ? tabModel.getFirstId() : active;
		Component component = componentResources.getContainerResources().getEmbeddedComponent(id);
		return component.getComponentResources().getBody();
	}
}