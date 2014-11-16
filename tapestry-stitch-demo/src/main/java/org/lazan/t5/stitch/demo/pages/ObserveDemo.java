package org.lazan.t5.stitch.demo.pages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

public class ObserveDemo {
	@Property
	private String message;
	
	@Property
	private List<Phone> phones;

	@InjectComponent
	private Zone phonesZone;
	
	public enum Color { RED, BLUE, BLACK, WHITE; }
	
	public enum Brand { APPLE, SAMSUNG, NOKIA; }
	
	public static class Phone {
		public Brand brand;
		public String name;
		public Color color;
		public BigDecimal price;
		
		public Phone(Brand brand, String name, Color color, BigDecimal price) {
			super();
			this.brand = brand;
			this.name = name;
			this.color = color;
			this.price = price;
		}
	}

	void setupRender() {
		phones = getAllPhones();
	}
	
	/**
	 * This event is fired by the observe mixin
	 */
	Block onFilter(String context, String name, Brand brand, Color color, String maxPrice) {
		BigDecimal parsedMaxPrice = parseBigDecimal(maxPrice);
		List<Phone> filtered = new ArrayList<Phone>();
		for (Phone phone : getAllPhones()) {
			boolean include = name == null || phone.name.toLowerCase().contains(name.toLowerCase());
			include &= color == null || color.equals(phone.color);
			include &= brand == null || brand.equals(phone.brand);
			include &= parsedMaxPrice == null || phone.price.compareTo(parsedMaxPrice) <= 0;
			if (include) {
				filtered.add(phone);
			}
		}
		message = "You changed: " + context;
		phones = filtered;
		return phonesZone.getBody();
	}
	
	BigDecimal parseBigDecimal(String s) {
		try {
			return s == null ? null : new BigDecimal(s);
		} catch (NumberFormatException e) {
			// ignore invalid decimals
			return null;
		}
	}
	
	protected List<Phone> getAllPhones() {
		return Arrays.asList(
			new Phone(Brand.APPLE, "iPhone 3", Color.BLACK, BigDecimal.valueOf(100)),
			new Phone(Brand.APPLE, "iPhone 4", Color.BLACK, BigDecimal.valueOf(200)),
			new Phone(Brand.APPLE, "iPhone 4", Color.WHITE, BigDecimal.valueOf(200)),
			new Phone(Brand.APPLE, "iPhone 5", Color.BLACK, BigDecimal.valueOf(300)),
			new Phone(Brand.APPLE, "iPhone 5", Color.WHITE, BigDecimal.valueOf(300)),
			new Phone(Brand.SAMSUNG, "Galaxy s2", Color.BLACK, BigDecimal.valueOf(50)),
			new Phone(Brand.SAMSUNG, "Galaxy s3", Color.BLUE, BigDecimal.valueOf(150)),
			new Phone(Brand.SAMSUNG, "Galaxy s3", Color.WHITE, BigDecimal.valueOf(150)),
			new Phone(Brand.SAMSUNG, "Galaxy s4", Color.BLUE, BigDecimal.valueOf(250)),
			new Phone(Brand.SAMSUNG, "Galaxy s4", Color.WHITE, BigDecimal.valueOf(250)),
			new Phone(Brand.SAMSUNG, "Galaxy s5", Color.BLUE, BigDecimal.valueOf(350)),
			new Phone(Brand.SAMSUNG, "Galaxy s5", Color.WHITE, BigDecimal.valueOf(350)),
			new Phone(Brand.NOKIA, "5110", Color.RED, BigDecimal.valueOf(40)),
			new Phone(Brand.NOKIA, "5110", Color.BLUE, BigDecimal.valueOf(41)),
			new Phone(Brand.NOKIA, "5110", Color.BLACK, BigDecimal.valueOf(42))
		);
	}
}
