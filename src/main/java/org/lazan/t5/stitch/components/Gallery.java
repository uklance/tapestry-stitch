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

public class Gallery<T> {
	@Parameter(required=true)
	@Property
	private GalleryDataModel<T> source;

	@Parameter(required=true)
	private int columns;

	@Parameter(required=true)
	private int rows;

	@Parameter(required=true)
	@Property
	private Block valueBlock;

	@Property
	private int page;

	@Property
	private List<T> row;

	@Parameter
	@Property
	private T value;

	@InjectComponent
	private Zone galleryZone;

	@SetupRender
	void setupRender() {
		page = 0;
	}

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

	@OnEvent("changePage")
	public Block onChangePage(int page) {
		this.page = page;
		return galleryZone.getBody();
	}
}
