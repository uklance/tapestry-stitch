package org.lazan.t5.stitch.model;

import org.apache.tapestry5.dom.Element;

public interface CellDecorator {
	public void decorate(Element cellElement, Object rowObject, int rowIndex, String propertyName, int colIndex);
}
