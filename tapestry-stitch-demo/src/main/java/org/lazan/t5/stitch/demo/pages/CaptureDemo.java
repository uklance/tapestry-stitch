package org.lazan.t5.stitch.demo.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.AfterRenderTemplate;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.stitch.model.MarkupHandler;

public class CaptureDemo {
	@Property
	private Integer current;
	
	// populated as the page template renders
	@Property
	private String countToFive;

	@Inject
	private JavaScriptSupport jss;
	
	@Inject
	private Block captureBlock;
	
	@AfterRenderTemplate
	void afterRenderTemplate() {
		// @AfterRenderTemplate is the first event where countToFive is populated
		jss.addScript("alert('%s')", countToFive.trim());
	}
	
	@OnEvent("ajaxEvent")
	Object onAjaxEvent() {
		return captureBlock;
	}
	
	public MarkupHandler getCountToTenHandler() {
		return new MarkupHandler() {
			
			// fired as the captureBlock renders
			public void handle(String markup) {
				jss.addScript("alert('%s')", markup.trim());
			}
		};
	}
}
