package org.lazan.t5.stitch.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.LibraryMapping;
import org.lazan.t5.stitch.binding.MapPropBindingFactory;

public class StitchModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(ProgressTaskManager.class, ProgressTaskManagerImpl.class);
	}

	public static void contributeComponentClassResolver(Configuration<LibraryMapping> config) {
		config.add(new LibraryMapping("stitch", "org.lazan.t5.stitch"));
	}
	
	public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> config) {
		config.addInstance("mapprop", MapPropBindingFactory.class);
	}
}
