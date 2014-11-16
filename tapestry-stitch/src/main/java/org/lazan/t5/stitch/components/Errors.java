package org.lazan.t5.stitch.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.corelib.internal.InternalMessages;

public class Errors {
	// Allow null so we can generate a better error message if missing
	@Environmental(false)
	private ValidationTracker tracker;
	
	void beginRender(MarkupWriter writer) {
		if (tracker == null)
			throw new RuntimeException(InternalMessages.encloseErrorsInForm());

		if (tracker.getHasErrors()) {
			writer.element("div", "class", "alert alert-error");
			for (String message : tracker.getErrors()) {
				writer.writeRaw("<strong>Error:</strong> ");
				writer.write(message);
				writer.writeRaw("<br/>");
			}
			writer.end();
		}
	}
}
