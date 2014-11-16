package org.lazan.t5.stitch.model;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class DefaultPagerModel implements PagerModel {
	private final int minStartPages;
	private final int minEndPages;
	private final int currentBuffer;
	private final int prevThreshold;
	private final int nextThreshold;
	
	/**
	 * A default pager model implementation
	 * @param minEndPages The minimum number of pages to display at the start and end of the pager
	 * @param currentBuffer Number of pages to display at either side of the current page
	 * @param nextPrevThreshold Minimum number of pages before the next and previous links are displayed
	 */
	public DefaultPagerModel(int minEndPages, int currentBuffer, int nextPrevThreshold) {
		this(minEndPages, minEndPages, currentBuffer, nextPrevThreshold, nextPrevThreshold);
	}

	/**
	 * A default pager model implementation
	 * @param minStartPages The minimum number of pages to display at the start of the pager
	 * @param minEndPages The minimum number of pages to display at the end of the pager
	 * @param currentBuffer Number of pages to display at either side of the current page
	 * @param prevThreshold Minimum number of pages before the previous link is displayed
	 * @param nextThreshold Minimum number of pages before the next link is displayed
	 */
	public DefaultPagerModel(int minStartPages, int minEndPages, int currentBuffer, int prevThreshold, int nextThreshold) {
		super();
		this.minStartPages = minStartPages;
		this.minEndPages = minEndPages;
		this.currentBuffer = currentBuffer;
		this.prevThreshold = prevThreshold;
		this.nextThreshold = nextThreshold;
	}
	
	public Collection<Integer> getPages(int currentPage, int pageCount) {
		Set<Integer> pages = new TreeSet<Integer>();
		
		// we will always display the same number of pages
		int requiredPages = minStartPages + minEndPages + 1 + (currentBuffer * 2);

		if (pageCount <= requiredPages) {
			addPages(pages, 1, pageCount, 1);
		} else {
			int bufferPages = 1 + (currentBuffer * 2);
			addPages(pages, 1, minStartPages, 1);
			addPages(pages, pageCount, minEndPages, -1);
			if (currentPage <= minStartPages) {
				// currentPage is within startPages
				// add extra pages to the start
				addPages(pages, minStartPages + 1, bufferPages, 1);
			} else if (currentPage >= pageCount - minEndPages) {
				// currentPage is within the endPages
				// add extra pages to the end
				addPages(pages, pageCount - minEndPages, bufferPages, -1);
			} else {
				// add buffer pages around currentPage
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
	
	public boolean isShowNext(int currentPage, int pageCount) {
		return currentPage < pageCount && pageCount >= nextThreshold;
	}
	
	public boolean isShowPrevious(int currentPage, int pageCount) {
		return currentPage > 1 && pageCount >= prevThreshold;
	}
}
