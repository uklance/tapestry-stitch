package org.lazan.t5.stitch.mixins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.stitch.services.internal.StringEventContext;

/**
 * This mixin allows you to observe any event (eg click, keypress) on any clientside element. The
 * clientside event triggers a serverside event. You can pass an optional context and a configurable
 * list of clientside field values to the serverside event.
 */
@Import(library = "Observe.js")
public class Observe {
	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	private String event;
	
	@Parameter(defaultPrefix=BindingConstants.LITERAL)
	private String clientEvent;
	
	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	private String zone;
	
	@Parameter
	private Object context;
	
	@Parameter
	private List<String> fields;
	
	@Inject
	private ComponentResources resources;
	
	@InjectContainer
	private ClientElement container;
	
	@Inject
	private JavaScriptSupport jss;
	
	@Inject
	private Request request;
	
	@Inject
	private ValueEncoderSource valueEncoderSource;
	
	void afterRender() {
		List<String> calculatedFields = calculateFields();
		String eventUrl = resources.createEventLink("observe", event, context, calculatedFields.size()).toURI();
		JSONObject spec = new JSONObject(
			"url", eventUrl,
			"event", getClientEvent(),
			"id", container.getClientId(),
			"zone", zone
		);
		if (calculatedFields != null) {
			spec.put("fieldIds", new JSONArray(calculatedFields.toArray()));
		}
		
		jss.addInitializerCall("observe", spec);
	}
	
	Object onObserve(String event, String context, int fieldCount) {
		List<String> contextValues = new ArrayList<String>();
		if (context != null) {
			contextValues.add(context);
		}
		for (int i = 0; i < fieldCount; ++ i) {
			String paramName = "observe" + i;
			contextValues.add(request.getParameter(paramName));
		}
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		EventContext eventContext = new StringEventContext(contextValues, valueEncoderSource);
		resources.triggerContextEvent(event, eventContext, callback);
		return callback.getResult();
	}
	
	String getClientEvent() {
		return clientEvent != null ? clientEvent : event;
	}
	
	List<String> calculateFields() {
		if (fields != null) {
			return fields;
		}
		if (container instanceof Field) {
			return Collections.singletonList(container.getClientId());
		}
		return Collections.emptyList();
	}
}
