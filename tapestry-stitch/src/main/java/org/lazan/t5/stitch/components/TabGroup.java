package org.lazan.t5.stitch.components;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.lazan.t5.stitch.model.TabGroupModel;

/**
 * NB: I would have preferred to use an Environmental instead of a request attribute but I can't
 * http://tapestry.1045711.n5.nabble.com/5-4-alpha-2-Environment-cloaked-during-ajax-component-event-td5719496.html
 */
public class TabGroup {
	public static final String ATTRIBUTE_MODEL = TabGroup.class.getName() + ".model";
	
	static class TabModel {
		String name;
		String label;
		Block body;
		
		TabModel(String name, String label, Block body) {
			super();
			this.name = name;
			this.label = label;
			this.body = body;
		}
	}
	
	@Property
	private Map<String, TabModel> tabModels;
	
	@Inject
	private Request request;
	
	@InjectComponent
	private Zone tabsZone;

	@Parameter
	private String active;
	
	@Property
	private String currentName;

	void setup() {
		tabModels = new LinkedHashMap<String, TabModel>();
		TabGroupModel tabGroupModel = new TabGroupModel() {
			public void addTab(String name, String label, Block body) {
				tabModels.put(name, new TabModel(name, label, body));
			}
		};
		request.setAttribute(ATTRIBUTE_MODEL, tabGroupModel);
	}

	TabModel getActiveTabModel() {
		TabModel activeModel = null;
		if (active != null) {
			activeModel = tabModels.get(active);
			if (activeModel == null) {
				throw new IllegalStateException("No such tab " + active);
			}
		} else if (!tabModels.isEmpty()) {
			// assume first tab is active if active parameter not specified
			activeModel = tabModels.values().iterator().next();
		}
		return activeModel;
	}

	@SetupRender
	void setupRender() {
		setup();
	}
	
	@CleanupRender
	void cleanupRender() {
		request.setAttribute(ATTRIBUTE_MODEL, null);
	}
	
	Object onTabChange(String tabName) {
		active = tabName;
		setup();
		return request.isXHR() ? tabsZone.getBody() : null;
	}
	
	public String getTabClass() {
		TabModel activeModel = getActiveTabModel();
		boolean isActive = activeModel != null && activeModel.name.equals(currentName);
		return isActive ? "active" : null;
	}
	
	public Block getActiveTabBody() {
		TabModel activeModel = getActiveTabModel();
		return activeModel == null ? null : activeModel.body;
	}
	
	public String getCurrentLabel() {
		return tabModels.get(currentName).label;
	}
}