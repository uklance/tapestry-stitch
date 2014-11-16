package org.lazan.t5.stitch.demo.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.lazan.t5.stitch.model.MapPropertyConduit;

public class MapGridDemo {
	@Inject 
	private BeanModelSource beanModelSource;
	
	@Inject
	private Messages messages;
	
	public Map[] getPeople() {
		return new Map[] {
			createPerson("Fred", "Flinstone", 34),
			createPerson("Wilma", "Flinstone", 29),
			createPerson("Barney", "Rubble", 25)
		};
	}
	
	private Map<String, Object> createPerson(String firstName, String lastName, int age) {
		Map<String, Object> person = new HashMap<String, Object>();
		person.put("firstName", firstName);
		person.put("lastName", lastName);
		person.put("age", age);
		return person;
	}
	
	public BeanModel<Object> getPeopleModel() {
		// initially construct a BeanModel for object (no properties)
		BeanModel<Object> beanModel = beanModelSource.createDisplayModel(Object.class, messages);
		
		// add MapPropertyConduits for each map entry
		beanModel.add("firstName", new MapPropertyConduit("firstName", String.class));
		beanModel.add("lastName", new MapPropertyConduit("lastName", String.class));
		beanModel.add("age", new MapPropertyConduit("age", int.class));
		
		return beanModel;
	}
}
