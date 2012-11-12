package org.lazan.t5.stitch.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.lazan.t5.stitch.model.GalleryDataModel;

/**
 * Renders a paged grid of items in a &lt;table&gt;
 */
public class Gallery<T> {
	// data source
	@Parameter(required=true)
	@Property
	private GalleryDataModel<T> source;

	// number of columns in the grid
	@Parameter(required=true)
	private int columns;

	// number of rows in the grid
	@Parameter(required=true)
	private int rows;

	// template for rendering each value
	@Parameter(required=true)
	@Property
	private Block valueBlock;

	// the current page being displayed (pageSize = rows * columns)
	@Property
	private int page;

	// the current grid row being rendered
	@Property
	private List<T> row;

	// the current value being rendered
	@Parameter
	@Property
	private T value;

	@InjectComponent
	private Zone galleryZone;

	@SetupRender
	void setupRender() {
		page = 0;
	}

	/**
	 * Retrieve the list of values for the current page and group them
	 * into rows and columns
	 */
	public List<List<T>> getGrid() {
		int pageSize = rows * columns;
		int startIndex = page * pageSize;
		Iterator<T> iterator = source.getItems(startIndex, pageSize).iterator();

		List<List<T>> rows = new ArrayList<List<T>>();
		while (iterator.hasNext()) {
			List<T> row = new ArrayList<T>(columns);
			for (int i = 0; i < columns; ++ i) {
				if (iterator.hasNext()) {
					row.add(iterator.next());
				} else {
					break;
				}
			}
			rows.add(row);
		}
		return rows;
	}

	public int getNextPage() {
		return page + 1;
	}

	public int getPrevPage() {
		return page - 1;
	}

	public boolean isShowNext() {
		int pageSize = rows * columns;
		int startIndex = (page + 1) * pageSize;
		return startIndex < source.size();
	}

	public boolean isShowPrev() {
		return page > 0;
	}

	/**
	 * Fired when changing pages. (pageSize = rows * columns)
	 * @param page The page of data to be displayed
	 */
	@OnEvent("changePage")
	public Block onChangePage(int page) {
		this.page = page;
		return galleryZone.getBody();
	}
}
