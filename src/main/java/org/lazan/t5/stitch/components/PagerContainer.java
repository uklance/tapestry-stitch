package org.lazan.t5.stitch.components;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.corelib.data.GridPagerPosition;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.stitch.model.PagerModel;

public class PagerContainer {
	@Parameter(required=true)
	@Property
	private Integer page;
	
	@Parameter(required=true)
	private GridDataSource source;

	@Parameter(required=true)
	private int pageSize;
	
	@Parameter(required=true)
	private List pageSource;
	
	@Parameter
	@Property
	private PagerModel pagerModel;
	
	@Parameter(value="symbol:stitch.pager.position")
	@Property
	private GridPagerPosition position;
	
	@InjectComponent
	private Zone zone;
	
	@Property
	private String zoneId;
	
	@Property
	private int pageCount;
	
	@Inject
	private ComponentResources resources;
	
	@Inject
	private JavaScriptSupport jss;
	
	@SetupRender
	void setupRender() {
		if (page == null) {
			page = 1;
		}
		zoneId = jss.allocateClientId(resources);
		init();
	}
	
	void init() {
		List tempPageSource = CollectionFactory.newList();
		int size = source.getAvailableRows();
		if (size > 0) {
			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(size - 1, startIndex + pageSize - 1);
			source.prepare(startIndex, endIndex, Collections.<SortConstraint> emptyList());
			
			for (int i = startIndex; i <= endIndex; ++i) {
				tempPageSource.add(source.getRowValue(i));
			}
		}
		pageCount = (int) Math.ceil(1d * size / pageSize);
		pageSource = tempPageSource;
	}

	Block onPage(int page, String zoneId) {
		this.page = page;
		this.zoneId = zoneId;
		init();
		return zone.getBody();
	}
}
