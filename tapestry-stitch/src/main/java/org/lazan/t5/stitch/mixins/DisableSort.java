package org.lazan.t5.stitch.mixins;

import java.util.List;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;

/**
 * Mixin to disable sorting on a grid
 */
@MixinAfter
public class DisableSort {
	@InjectContainer
	private Grid grid;
	
	@SetupRender
	void setupRender() {
		BeanModel beanModel = grid.getDataModel();
		List<String> columns = beanModel.getPropertyNames();
		for (String column : columns) {
			beanModel.get(column).sortable(false);
		}
	}
}
