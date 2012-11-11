package org.lazan.t5.stitch.mixins;

import java.util.List;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.lazan.t5.stitch.model.GridCellDecorator;
import org.lazan.t5.stitch.model.GridRowDecorator;

/**
 * Applies decorators to rows and cells in a Grid
 */
@MixinAfter
public class GridDecorator {
	@InjectContainer
	private Grid grid;
	
	@Parameter
	private List<GridRowDecorator> rowDecorators;
	
	@Parameter
	private List<GridCellDecorator> cellDecorators;
	
	@AfterRender
	void afterRender(MarkupWriter writer) {
		List<Node> topChildren = writer.getElement().getChildren();
		Element containingDiv = (Element) topChildren.get(topChildren.size() - 1);
		List<Node> rows = containingDiv.find("table/tbody").getChildren();
		List<String> propertyNames = grid.getDataModel().getPropertyNames();

		// grid pages start at 1
		int startIndex = (grid.getCurrentPage() - 1) * grid.getRowsPerPage();

		int trIndex = 0;
		for (Node rowNode : rows) {
			Element rowElement = (Element) rowNode;
			int dataIndex = startIndex + trIndex;
			Object rowValue = grid.getDataSource().getRowValue(dataIndex);
			if (rowDecorators != null) {
				for (GridRowDecorator rowDecorator : rowDecorators) {
					rowDecorator.decorate(rowElement, rowValue, trIndex);
				}
			}
			if (cellDecorators != null) {
				List<Node> cells = rowElement.getChildren();
				int tdIndex = 0;
				for (String propertyName : propertyNames) {
					Element cellElement = (Element) cells.get(tdIndex);
					for (GridCellDecorator cellDecorator : cellDecorators) {
						cellDecorator.decorate(cellElement, rowValue, trIndex, propertyName, tdIndex);
					}
					++ tdIndex;
				}
			}
			++ trIndex;
		}
	}
}
