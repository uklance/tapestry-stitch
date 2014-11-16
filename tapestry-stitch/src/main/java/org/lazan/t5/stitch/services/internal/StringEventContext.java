package org.lazan.t5.stitch.services.internal;

import java.util.List;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderSource;

/**
 * EventContext implementation which can convert strings to objects by looking up a {@link ValueEncoder}.
 * Useful for situations when serverside values start their life as strings (eg request parameters)
 */
public class StringEventContext implements EventContext {
	private final List<String> strings;
	private final ValueEncoderSource valueEncoderSource;
	public StringEventContext(List<String> strings,
			ValueEncoderSource valueEncoderSource) {
		super();
		this.strings = strings;
		this.valueEncoderSource = valueEncoderSource;
	}
	
	/**
	 * Lookup a {@link ValueEncoder} and convert the string to an object.
	 */
	@Override
	public <T> T get(Class<T> type, int index) {
		ValueEncoder<T> encoder = valueEncoderSource.getValueEncoder(type);
		return encoder.toValue(strings.get(index));
	}
	
	@Override
	public int getCount() {
		return strings.size();
	}
	
	@Override
	public String[] toStrings() {
		return (String[]) strings.toArray(new String[strings.size()]);
	}
}
