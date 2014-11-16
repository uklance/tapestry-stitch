package org.lazan.t5.stitch.model;

import org.apache.tapestry5.dom.Element;

public interface GridCellDecorator {
	/**
	 * This method is applied to each cell in the grid
	 * @param cellElement The DOM element for the &lt;td&gt;
	 * @param rowObject The row data
	 * @param rowIndex The index of the &lt;tr&gt; in the DOM
	 * @param propertyName The property being rendered
	 * @param colIndex The index of the &lt;td&gt; in the DOM
	 */
	public void decorate(Element cellElement, Object rowObject, int rowIndex, String propertyName, int colIndex);
}
