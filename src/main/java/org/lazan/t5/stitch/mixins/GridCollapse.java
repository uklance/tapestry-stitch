package org.lazan.t5.stitch.mixins;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;

@MixinAfter
public class GridCollapse {
	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	private String collapseColumn;
	
	@InjectContainer
	private Grid grid;
	
	@CleanupRender
	void cleanupRender(MarkupWriter writer) {
		List<String> propertyNames = grid.getDataModel().getPropertyNames();
		int collapseIndex = propertyNames.indexOf(collapseColumn);
		if (collapseIndex < 0)  throw new RuntimeException("No such column " + collapseColumn);

		Element parentTable = findTable(writer.getElement());
		List<Node> parentRows = parentTable.find("tbody").getChildren();
		
		boolean isFirst = true;
		for (Node parentRowNode : parentRows) {
			Element parentRow = (Element) parentRowNode;
			List<Node> parentCells = parentRow.getChildren();
			
			Element parentCell = (Element) parentCells.get(collapseIndex);
			Element childTable = findTable(parentCell);
			
			if (isFirst) {
				// copy the header from the first child
				collapseHeaders(collapseIndex, parentTable, childTable);
				isFirst = false;
			}
			
			setRowSpans(parentCells, childTable);
			collapseBody(parentRow, parentCell, childTable);
		}
	}

	/**
	 * Move the cells from the child table to the parent table
	 */
	private void collapseBody(Element parentRow, Element parentCell, Element childTable) {
		List<Node> childRows = childTable.find("tbody").getChildren();
		
		Element prevRow = null;
		for (Node childRowNode : childRows) {
			Element childRow = (Element) childRowNode;
			
			if (prevRow == null) {
				for (Node cell : childRow.getChildren()) {
					cell.moveBefore(parentCell);
				}
				prevRow = parentRow;
			} else {
				childRow.moveAfter(prevRow);
				prevRow = childRow;
			}
		}
		
		parentCell.remove();
	}

	/**
	 * Move the headers from the child table to the parent table and remove the placeholder header
	 */
	private void collapseHeaders(int collapseIndex, Element parentTable, Element childTable) {
		List<Node> parentHeaderCells = getHeaderCells(parentTable);
		List<Node> collapseHeaderCells = getHeaderCells(childTable);
		Element parentHeader = (Element) parentHeaderCells.get(collapseIndex);

		for (Node collapseHeader : collapseHeaderCells) {
			collapseHeader.moveBefore(parentHeader);
		}
		parentHeader.remove();
	}
	
	private List<Node> getHeaderCells(Element table) {
		Element thead = (Element) table.find("thead");
		Element headRow = (Element) thead.getChildren().get(0);
		return headRow.getChildren();
	}
	
	/**
	 * Set rowspans on all of the cells in the parent table
	 */
	private void setRowSpans(List<Node> parentCells, Element childTable) {
		List<Node> childRows = childTable.find("tbody").getChildren();
		String rowSpan = childRows.size() > 1 ? String.valueOf(childRows.size()) : null;
		for (Node parentCellNode : parentCells) {
			Element parentCell = (Element) parentCellNode;
			parentCell.attribute("rowspan", rowSpan);
		}
	}
	
	/**
	 * Find the table tag rendered by a tapestry grid
	 * @param container DOM element
	 * @return the table DOM element
	 */
	private Element findTable(Element container) {
		List<Node> topChildren = container.getChildren();
		Node topChild = topChildren.get(topChildren.size() - 1);
		if (!(topChild instanceof Element)) {
			throw new RuntimeException(String.format("Expected Element, found %s, (%s)", topChild.getClass(), topChild));
		}
		Element table = ((Element) topChild).find("table");
		if (table == null) {
			throw new RuntimeException("Could not find table element");
		}
		return table;
	}
}
