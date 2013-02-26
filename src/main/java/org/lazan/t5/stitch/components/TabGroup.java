package org.lazan.t5.stitch.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
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
	
	@InjectComponent
	private Zone tabsZone;

	@Property
	private TabModel tabModel;
	
	@Parameter
	private Integer active;
	
	@Property
	private String tabLabel;
	
	@Property
	private int currentIndex;

	@SetupRender
	void setupRender() {
		// assume first tab is active if active parameter not specified
		setup(active == null ? 0 : active);
	}
	
	void setup(int activeIndex) {
		tabModel = new TabModel(activeIndex);
		request.setAttribute(ATTRIBUTE_TAB_MODEL, tabModel);
	}
	
	@CleanupRender
	void cleanupRender() {
		request.setAttribute(ATTRIBUTE_TAB_MODEL, null);
	}
	
	Object onTabChange(int activeIndex) {
		active = activeIndex;
		setup(activeIndex);
		return request.isXHR() ? tabsZone.getBody() : null;
	}
	
	public String getTabClass() {
		return tabModel.getActiveTabIndex() == currentIndex ? "active" : null;
	}
	
	public RenderCommand getActiveTabBody() {
		return new RenderCommand() {
			public void render(MarkupWriter writer, RenderQueue queue) {
				writer.writeRaw(tabModel.getActiveTabBody());
			}
		};
	}
}