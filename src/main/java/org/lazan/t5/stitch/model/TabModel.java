package org.lazan.t5.stitch.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
 
public class TabModel {
	private Map<String, String> labelsByName = new LinkedHashMap<String, String>();
	private String activeTabBody;
	private String activeTabName;
	
	public TabModel(String activeTabName) {
		super();
		this.activeTabName = activeTabName;
	}
	
	public boolean containsName(String name) {
		return labelsByName.containsKey(name);
	}
	
	public void addTab(String name, String label) {
		labelsByName.put(name, label);
	}

	public String getActiveTabBody() {
		return activeTabBody;
	}

	public void setActiveTabBody(String activeTabBody) {
		this.activeTabBody = activeTabBody;
	}

	public boolean isActive(String name) {
		boolean active = false;
		if (activeTabName != null) {
			active = activeTabName.equals(name);
		} else if (!labelsByName.isEmpty()) {
			String firstName =  labelsByName.keySet().iterator().next();
			active = firstName.equals(name);
		}
		return active;
	}
	
	public Collection<String> getNames() {
		return labelsByName.keySet();
	}
	
	public String getLabel(String name) {
		return labelsByName.get(name);
	}
}