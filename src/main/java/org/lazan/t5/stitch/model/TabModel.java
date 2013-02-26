package org.lazan.t5.stitch.model;

import java.util.ArrayList;
import java.util.List;
 
public class TabModel {
	private List<String> labels = new ArrayList<String>();
	private String activeTabBody;
	private int activeTabIndex;
	
	public TabModel(int activeTabIndex) {
		super();
		this.activeTabIndex = activeTabIndex;
	}

	/**
	 * Add a label to the model and return the index of the current tab
	 * @param label Label to display for the current tab
	 * @return The index of the current tab
	 */
	public int addLabel(String label) {
		labels.add(label);
		return labels.size() - 1;
	}

	public String getActiveTabBody() {
		return activeTabBody;
	}

	public void setActiveTabBody(String activeTabBody) {
		this.activeTabBody = activeTabBody;
	}

	public List<String> getLabels() {
		return labels;
	}
	
	public int getActiveTabIndex() {
		return activeTabIndex;
	}
}