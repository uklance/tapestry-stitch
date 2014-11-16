package org.lazan.t5.stitch.demo.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.lazan.t5.stitch.demo.entities.Category;
import org.lazan.t5.stitch.demo.entities.Item;

/**
 * Creates some hierarchical test data
 * @author Lance
 */
public class ItemCreator {
	private final Session session;
	
	public ItemCreator(Session session) {
		super();
		this.session = session;
	}

	public void createItemsAndCategories() {
		String[] categories = {
			"Food/Meat/Fish",
			"Food/Meat/Chicken",
			"Food/Fruit/Apple",
			"Food/Fruit/Orange",
			"Food/Vegetable/Potato",
			"Drink/Alcoholic/Spirits",
			"Drink/Alcoholic/Beer",
			"Drink/Soft Drink"
		};
		
		String[] items = {
			"Fish|Breaded Plaice|2.30|TODO: populate",
			"Fish|500g Salmon|3.20|TODO: populate",
			"Fish|10 pack of fish fingers|3.50|TODO: populate",
			"Chicken|10 chicken drumsticks|5.25|TODO: populate",
			"Chicken|Whole chicken|4.00|TODO: populate",
			"Apple|Bag of granny smith apples|3.00|TODO: populate",
			"Apple|Single granny smith apple|0.30|TODO: populate",
			"Orange|Bag of oranges|3.25|TODO: populate",
			"Potato|Bag of potatoes|4.20|TODO: populate",
			"Spirits|750ml Smirnoff vodka|12.20|TODO: populate",
			"Spirits|750ml Johnnie Walker blue label|130|TODO: populate",
			"Spirits|750ml Johnnie Walker black label|18|TODO: populate",
			"Beer|24 cans of Carling beer|10.25|TODO: populate",
			"Beer|1 can of Carling beer|1.50|TODO: populate",
			"Soft Drink|Can of Coke|0.60|TODO: populate",
			"Soft Drink|600ml bottle of Coke|1.20|TODO: populate",
		};
		
		Map<String, Category> catCache = new HashMap<String, Category>();
		addCategories(catCache, categories);
		addItems(catCache, items);
	}

	private void addCategories(Map<String, Category> catCache, String[] categories) {
		for (String categoryEntry : categories) {
			String[] hierarchy = categoryEntry.split("/");
			Category parentCategory = null;
			for (String categoryName : hierarchy) {
				Category category = catCache.get(categoryName);
				if (category == null) {
					category = new Category();
					category.setName(categoryName);
					category.setParentCategory(parentCategory);
					session.save(category);
					
					catCache.put(categoryName, category);
				}
				parentCategory = category;
			}
		}
	}

	private void addItems(Map<String, Category> catCache, String[] items) {
		for (String itemString : items) {
			String[] row = itemString.split("\\|");

			Category category = catCache.get(row[0]);
			Item item = new Item();
			item.setCategory(category);
			item.setName(row[1]);
			item.setPrice(new BigDecimal(row[2]));
			item.setDescription(row[3]);
			session.save(item);
		}
	}
}
