package org.lazan.t5.stitch.demo.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.dom.Element;
import org.lazan.t5.stitch.model.GridCellDecorator;
import org.lazan.t5.stitch.model.GridRowDecorator;

public class GridDecoratorDemo {
	public static final String BLUE = "#3a87ad";
	public static final String RED = "#b94a48";
	public static final String YELLOW = "#c09853";
	public static final String GREEN = "#468847";
	
	public static class Item {
		public int value;
		public int valueTimesTwo;
		public int valueTimesTen;
	}

	public List<Item> getItems() {
		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < 100; ++i) {
			int value = i + 1;
			
			Item item = new Item();
			item.value = value;
			item.valueTimesTwo = value * 2;
			item.valueTimesTen = value * 10;
			
			items.add(item);
		}
		return items;
	}
	
	/**
	 * Display an alert when any row is clicked
	 */
	public GridRowDecorator getRowDecorator() {
		return new GridRowDecorator() {
			public void decorate(Element element, Object rowValue, int rowIndex) {
				Item item = (Item) rowValue;
				String script = String.format("alert('value=%s, rowIndex=%s')", item.value, rowIndex);
				element.attribute("onclick", script);
			}
		};
	}

	/**
	 * Set the background color of every cell
	 */
	public GridCellDecorator getCellDecorator() {
		return new GridCellDecorator() {
			public void decorate(Element cellElement, Object rowObject, int rowIndex, String propertyName, int colIndex) {
				String color;
				if (rowIndex % 2 == 0) {
					color = colIndex % 2 == 0 ? YELLOW : RED;
				} else {
					color = colIndex % 2 == 0 ? GREEN : BLUE;
				}
				cellElement.attribute("style", "background-color:" + color);
			}
		};
	}
}
