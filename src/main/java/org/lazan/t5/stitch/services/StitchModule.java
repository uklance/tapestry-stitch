package org.lazan.t5.stitch.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.LibraryMapping;
import org.lazan.t5.stitch.binding.MapPropBindingFactory;
import org.lazan.t5.stitch.model.Syntax;

public class StitchModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(ProgressTaskManager.class, ProgressTaskManagerImpl.class);
		binder.bind(SyntaxSource.class, SyntaxSourceImpl.class);
	}

	public static void contributeComponentClassResolver(Configuration<LibraryMapping> config) {
		config.add(new LibraryMapping("stitch", "org.lazan.t5.stitch"));
	}
	
	public static void contributeBindingSource(MappedConfiguration<String, BindingFactory> config) {
		config.addInstance("mapprop", MapPropBindingFactory.class);
	}
	
	public void contributeSyntaxSource(MappedConfiguration<String, Syntax> config) {
		config.add("tml", Syntax.XML);
		config.add("xml", Syntax.XML);
		config.add("js", Syntax.JAVASCRIPT);
		config.add("java", Syntax.JAVA);
	}
}
