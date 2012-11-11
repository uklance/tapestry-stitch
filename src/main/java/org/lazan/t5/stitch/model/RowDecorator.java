package org.lazan.t5.stitch.model;

import org.apache.tapestry5.dom.Element;

public interface RowDecorator {
	public void decorate(Element element, Object rowValue, int rowIndex);
}
