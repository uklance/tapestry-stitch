package org.lazan.t5.stitch.components;

import java.util.Collection;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.lazan.t5.stitch.model.PagerModel;

public class Pager {
	@Parameter(required=true)
	private PagerModel model;
	
	/**
	 * The current page being displayed
	 */
	@Parameter(required=true)
	private int page;
	
	/**
	 * The total page count
	 */
	@Parameter(required=true)
	private int pageCount;
	
	@Property
	@Parameter(required=true)
	private String zone;
	
	@Property
	private Collection<Integer> pagerPages;
	
	/**
	 * The current page link being rendered
	 */
	@Property(write=false)
	private int pagerPage;
	
	@Property
	private int index;
	
	@Property
	private boolean showSpacer;
	
	@SetupRender
	void setupRender() {
		pagerPages = model.getPages(page, pageCount);
	}
	
	public boolean isShowPrev() {
		return model.isShowPrevious(page, pageCount);
	}

	public int getPrevPage() {
		return page - 1;
	}

	public boolean isShowNext() {
		return model.isShowNext(page, pageCount);
	}
	
	public int getNextPage() {
		return page + 1;
	}
	
	public String getLinkClass() {
		return page == pagerPage ? "active" : null;
	}
	
	/**
	 * Iterate to the next page link. Re-calculate showSpacer
	 */
	public void setPagerPage(int pagerPage) {
		showSpacer = index > 0 && (pagerPage - this.pagerPage) > 1;
		this.pagerPage = pagerPage;
	}
}
