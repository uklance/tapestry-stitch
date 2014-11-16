package org.lazan.t5.stitch.demo.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.grid.CollectionGridDataSource;
import org.lazan.t5.stitch.model.DefaultPagerModel;
import org.lazan.t5.stitch.model.PagerModel;

public class PagerDemo {
	@Property
	private Integer page;
	
	@Property
	private List<String> pageRecords;
	
	@Property
	private String record;
	
	public GridDataSource getAllRecords() {
		List<String> allRecords = new ArrayList<String>();
		for (int i = 0; i < 150; ++ i) {
			allRecords.add("Record " + (i + 1));
		}
		return new CollectionGridDataSource(allRecords);
	}
	
	public PagerModel getPagerModel() {
		return new DefaultPagerModel(2, 1, 2);
	}
}
