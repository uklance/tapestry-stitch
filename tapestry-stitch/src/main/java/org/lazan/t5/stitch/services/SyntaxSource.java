package org.lazan.t5.stitch.services;

import org.apache.tapestry5.Asset;
import org.lazan.t5.stitch.model.Syntax;

public interface SyntaxSource {
	Syntax getSyntax(Asset source);
}
