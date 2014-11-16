package org.lazan.t5.stitch.field;

import org.apache.tapestry5.Field;

/**
 * Represents a field at a snapshot in time. Useful when you want to record errors
 * for a field that is re-used in a loop
 */
public class FieldSnapshot implements Field {
	private String clientId;
	private String controlName;
	private String label;
	private boolean required;
	private boolean disabled;
	
	public FieldSnapshot(Field field) {
		clientId = field.getClientId();
		controlName = field.getControlName();
		label = field.getLabel();
		required = field.isRequired();
		disabled = field.isDisabled();
	}
	
	public String getClientId() {
		return clientId;
	}
	public String getControlName() {
		return controlName;
	}
	public String getLabel() {
		return label;
	}
	public boolean isRequired() {
		return required;
	}
	public boolean isDisabled() {
		return disabled;
	}
}
