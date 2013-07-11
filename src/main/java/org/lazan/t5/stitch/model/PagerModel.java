package org.lazan.t5.stitch.model;

import java.util.Collection;

public interface PagerModel {
	public Collection<Integer> getPages(int currentpage, int pageCount);
	public boolean isShowPrevious(int currentpage, int pageCount);
	public boolean isShowNext(int currentpage, int pageCount);
}
