package org.lazan.t5.stitch.demo.pages;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.lazan.t5.stitch.field.FieldSnapshot;

public class MapBindingDemo {
	private static final String[] FIELD_NAMES = { "field1", "field2", "field3" };
	
	private static final Pattern CONTROL_NAME_PATTERN = Pattern.compile(".*_(.*)");

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Map<String, String> fieldValues;
	
	@Property
	private String fieldName;
	
	@InjectComponent
	private TextField genericField;
	
	@InjectComponent
	private Form form;
	
	@Persist(PersistenceConstants.FLASH)
	@Property
	private boolean submitted;

	@SetupRender
	void setupRender() {
		if (!submitted) {
			fieldValues = new LinkedHashMap<String, String>();
		}
	}
	
	void onPrepareForSubmit() {
		fieldValues = new LinkedHashMap<String, String>();
		submitted = true;
	}
	
	void onValidateFromGenericField(String value) {
		if (value == null || !value.toLowerCase().startsWith("a")) {
			String fieldName = getFieldName(genericField.getControlName());
			Field fieldSnapshot = new FieldSnapshot(genericField);
			form.recordError(fieldSnapshot, fieldName + " must start with 'a'");
		}
	}
	
	/**
	 * Reverse engineer the field name from the tapestry generated control name
	 */
	String getFieldName(String controlName) {
		Matcher matcher = CONTROL_NAME_PATTERN.matcher(controlName);
		int index = 0;
		if (matcher.matches()) {
			int suffix = Integer.parseInt(matcher.group(1));
			index = suffix + 1;
		}
		return FIELD_NAMES[index];
	}
	
	public String[] getFieldNames() {
		return FIELD_NAMES;
	}
}
