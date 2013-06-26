package org.lazan.t5.stitch.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.lazan.t5.stitch.model.Syntax;

public class CodeTab {
	@Parameter(defaultPrefix=BindingConstants.LITERAL)
	private Syntax syntax;
	
	@Parameter(required=true, defaultPrefix=BindingConstants.ASSET)
	@Property
	private Asset source;
	
	public String getName() {
		return source.getResource().getFile();
	}
}
