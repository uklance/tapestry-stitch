package org.lazan.t5.stitch.model;

import java.util.LinkedHashMap;
import java.util.Map;
 
public class TabModel {
	private Map<String, String> labelsById = new LinkedHashMap<String, String>();
	
	public Map<String, String> getLabelsById() {
		return labelsById;
	}
	
	public String getFirstId() {
		return labelsById.keySet().iterator().next();
	}
}