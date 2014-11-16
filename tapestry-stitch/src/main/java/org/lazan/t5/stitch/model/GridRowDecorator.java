package org.lazan.t5.stitch.model;

import org.apache.tapestry5.dom.Element;

public interface GridRowDecorator {
	/**
	 * This is applied to each row in the grid
	 * @param element The DOM element for the &lt;tr&gt;
	 * @param rowValue The row data
	 * @param rowIndex The index of the &lt;tr&gt; in the DOM
	 */
	public void decorate(Element element, Object rowValue, int rowIndex);
}
