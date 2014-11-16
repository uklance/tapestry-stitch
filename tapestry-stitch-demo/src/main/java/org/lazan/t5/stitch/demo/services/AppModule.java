package org.lazan.t5.stitch.demo.services;

import org.apache.tapestry5.ComponentParameterConstants;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.lazan.t5.stitch.services.StitchModule;

@SubModule(StitchModule.class)
public class AppModule {
	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
		configuration.add(ComponentParameterConstants.ZONE_UPDATE_METHOD, "show");
	}
	
	@Startup
	public void createItemsAndCategories(@Autobuild ItemCreator itemCreator, HibernateSessionManager hibernateSessionManager) {
		itemCreator.createItemsAndCategories();
		hibernateSessionManager.commit();
	}
}
