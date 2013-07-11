package org.lazan.t5.stitch.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class DefaultPagerModel implements PagerModel {
	private final int minStartPages;
	private final int minEndPages;
	private final int currentBuffer;
	private final int showPreviousThreshold;
	private final int showNextThreshold;
	
	/**
	 * A default pager model implementation
	 * @param minStartPages The minimum number of pages to display at the start of the pager
	 * @param minEndPages The minimum number of pages to display at the end of the pager
	 * @param currentBuffer Number of pages to display at either side of the current page
	 * @param showPreviousThreshold Minimum number of pages before the previous link is displayed
	 * @param showNextThreshold Minimum number of pages before the next link is displayed
	 */
	public DefaultPagerModel(int minStartPages, int minEndPages, int currentBuffer, int showPreviousThreshold, int showNextThreshold) {
		super();
		this.minStartPages = minStartPages;
		this.minEndPages = minEndPages;
		this.currentBuffer = currentBuffer;
		this.showPreviousThreshold = showPreviousThreshold;
		this.showNextThreshold = showNextThreshold;
	}
	
	public Collection<Integer> getPages(int currentPage, int pageCount) {
		Set<Integer> pages = new TreeSet<Integer>();
		int requiredPages = minStartPages + minEndPages + 1 + (currentBuffer * 2);

		if (pageCount <= requiredPages) {
			addPages(pages, 1, pageCount, 1);
		} else {
			int bufferPages = 1 + (currentBuffer * 2);
			addPages(pages, 1, minStartPages, 1);
			addPages(pages, pageCount, minEndPages, -1);
			if (currentPage <= minStartPages) {
				addPages(pages, minStartPages + 1, bufferPages, 1);
			} else if (currentPage >= pageCount - minEndPages) {
				addPages(pages, pageCount - minEndPages, bufferPages, -1);
			} else {
				int start = Math.max(minStartPages + 1, currentPage - currentBuffer);
				addPages(pages, start, bufferPages, 1);
			}
		}
		return pages;
	}
	
	private void addPages(Set<Integer> pages, int start, int count, int increment) {
		for (int i = 0; i < count; ++ i) {
			int page = start + (increment * i);
			pages.add(page);
		}
	}
	
	public boolean isShowNext(int currentpage, int pageCount) {
		return pageCount >= showNextThreshold;
	}
	
	public boolean isShowPrevious(int currentpage, int pageCount) {
		return pageCount >= showPreviousThreshold;
	}
}
