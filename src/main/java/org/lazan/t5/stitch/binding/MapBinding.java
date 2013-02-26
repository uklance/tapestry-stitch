package org.lazan.t5.stitch.binding;

import java.util.Map;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

public class MapBinding extends AbstractBinding {
    private final Object root;
    private final PropertyConduit mapConduit;
    private final PropertyConduit keyConduit;
    private final String toString;

    public MapBinding(Location location, Object root, PropertyConduit mapConduit, PropertyConduit keyConduit, String toString) {
        super(location);
        this.root = root;
        this.mapConduit = mapConduit;
        this.keyConduit = keyConduit;
        this.toString = toString;
    }
    
    @SuppressWarnings("rawtypes")
	public Object get() {
    	try {
	    	Map map = (Map) mapConduit.get(root);
	    	if (map != null) {
	    		Object key = keyConduit.get(root);
	    		return map.get(key);
	    	}
	    	return null;
        } catch (Exception e) {
            throw new TapestryException(e.getMessage(), getLocation(), e);
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void set(Object value) {
    	try {
	    	Map map = (Map) mapConduit.get(root);
	    	if (map == null) {
	    		throw new NullPointerException("Attempting to update a null Map");
	    	}
	    	Object key = keyConduit.get(root);
	    	map.put(key, value);
        } catch (Exception e) {
            throw new TapestryException(e.getMessage(), getLocation(), e);
        }
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public Class getBindingType() {
    	return Object.class; 
    }
    
    @Override
    public String toString() {
    	return toString;
    }
    
    @Override
    public boolean isInvariant() {
    	return false;
    }
}
