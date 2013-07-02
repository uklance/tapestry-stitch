package org.lazan.t5.stitch.components;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

public class BubbleParent {
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String message;
	
	void onEvent1() {
		message = "Event 1 fired on parent";
	}	
}
