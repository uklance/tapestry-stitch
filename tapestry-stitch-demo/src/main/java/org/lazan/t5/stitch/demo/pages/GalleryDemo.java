package org.lazan.t5.stitch.demo.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.lazan.t5.stitch.model.GalleryDataModel;

public class GalleryDemo {
	@Inject
	private AssetSource assetSource;

	@Property
	private Asset image;

	public GalleryDataModel<Asset> getImages() {
		return new GalleryDataModel<Asset>() {
			public int size() {
				return 15;
			}
			public List<Asset> getItems(int startIndex, int maxItems) {
				List<Asset> assets = new ArrayList<Asset>();
				int end = Math.min(size(), startIndex + maxItems);
				for (int i = startIndex; i < end; ++ i) {
					String path = String.format("images/%s.jpg", i);
					assets.add(assetSource.getContextAsset(path, null));
				}
				return assets;
			}
		};
	}
}
