package org.lazan.t5.stitch.mixins;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = "OnEvent.js")
public class OnEvent {
	public static final String FIELD_DELIMITER = ",";
	
	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	private String event;
	
	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	private String zone;
	
	@Parameter(defaultPrefix=BindingConstants.LITERAL)
	private String clientEvent;
	
	@Parameter
	private Object context;
	
	@Parameter
	private String[] fields;
	
	@Inject
	private ComponentResources resources;
	
	@InjectContainer
	private ClientElement container;
	
	@Inject
	private JavaScriptSupport jss;
	
	@Inject
	private Request request;
	
	void afterRender() {
		String[] fieldIdsConfig;
		if (fields != null) {
			fieldIdsConfig = fields;
		} else if (container instanceof Field) {
			fieldIdsConfig = new String[] { container.getClientId() };
		} else {
			fieldIdsConfig = new String[0];
		}
		String csvFieldIds = delimit(fieldIdsConfig, FIELD_DELIMITER);
		String urlConfig = resources.createEventLink("clientEvent", event, context, csvFieldIds).toURI();
		String clientEventConfig = clientEvent == null ? event : clientEvent;
		JSONObject spec = new JSONObject(
			"url", urlConfig,
			"event", clientEventConfig,
			"id", container.getClientId(),
			"zone", zone
		);
		if (fieldIdsConfig != null) {
			spec.put("fieldIds", new JSONArray((Object[]) fieldIdsConfig));
		}
		
		jss.addInitializerCall("onEvent", spec);
	}
	
	private String delimit(String[] arr, String delemiter) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < arr.length; ++ i) {
			if (i != 0) result.append(delemiter);
			String value = arr[i];
			if (value.contains(delemiter)) {
				throw new RuntimeException(
						String.format("Illegal field '%s'. Fields can not contain '%s'", value, delemiter));
			}
			result.append(arr[i]);
		}
		return result.toString();
	}

	Object onClientEvent(String event, String context, String csvFieldIds) {
		List<Object> contextValues = new ArrayList<Object>();
		String[] fieldIds = csvFieldIds.split(FIELD_DELIMITER);
		if (context != null) {
			contextValues.add(context);
		}
		for (Object fieldId : fieldIds) {
			String paramName = "onEvent." + fieldId;
			contextValues.add(request.getParameter(paramName));
		}
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(event, contextValues.toArray(), callback);
		return callback.getResult();
	}
}
