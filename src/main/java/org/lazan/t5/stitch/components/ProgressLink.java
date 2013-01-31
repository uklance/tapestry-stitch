package org.lazan.t5.stitch.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.stitch.model.ProgressTask;
import org.lazan.t5.stitch.services.ProgressTaskManager;

@Import(library="ProgressLink.js")
public class ProgressLink {
	@Inject
	private ProgressTaskManager taskManager;
	
	@Inject
	private AjaxResponseRenderer ajaxRenderer;
	
	@Inject
	private ComponentResources resources;
	
	@Inject
	private JavaScriptSupport jss;

	@Parameter(required=true)
	private ProgressTask task;
	
	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	@Property
	private Block label;
	
	@Property
	private String progressId;
	
	@Property
	private String zoneId;
	
	@SetupRender
	void setupRender() {
		progressId = jss.allocateClientId("progress");
		zoneId = jss.allocateClientId("zone");
	}
	
	void onStart(final String progressId, final String zoneId) {
		int taskId = taskManager.add(task);
		updateProgress(progressId, zoneId, taskId, 0);
	}
	
	void onUpdateProgress(final String progressId, final String zoneId, final int taskId) {
		float progress = taskManager.getProgress(taskId);
		updateProgress(progressId, zoneId, taskId, progress);
	}
	
	void updateProgress(final String progressId, final String zoneId, final int taskId, final float progress) {
		ajaxRenderer.addCallback(new JavaScriptCallback() {
			public void run(JavaScriptSupport jss) {
				Link link = resources.createEventLink("updateProgress", progressId, zoneId, taskId);
				JSONObject args = new JSONObject();
				args.put("link", link.toString());
				args.put("taskId", taskId);
				args.put("progress", progress);
				args.put("progressId", progressId);
				args.put("zoneId", zoneId);
				String script = "updateProgress(" + args.toString() + ")";
				jss.addScript(script);
			}
		});
	}
}
