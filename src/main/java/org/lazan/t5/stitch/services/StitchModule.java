package org.lazan.t5.stitch.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.services.LibraryMapping;

public class StitchModule {
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("stitch", "org.lazan.t5.stitch"));
	}
}
