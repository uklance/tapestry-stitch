package org.lazan.t5.stitch.model;

import java.util.List;

public interface GalleryDataModel<T> {
	int size();
	List<T> getItems(int startIndex, int maxItems);
}
