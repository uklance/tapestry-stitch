package org.lazan.t5.stitch.demo.model;

import org.lazan.t5.stitch.demo.entities.Category;
import org.lazan.t5.stitch.demo.entities.Item;

/**
 * A node in a hierarchy of {@link Category} and {@link Item}. Each category may contain 
 * sub-categories and sub-items. {@link Item}s have no children and are leaves of the tree.
 * 
 * @author Lance
 */
public class ItemTreeNode {
	private Item item;
	private Category category;
	private int childItemCount = 0;
	private int childCategoryCount = 0;
	
	/**
	 * Item constructor
	 * @param item
	 */
	public ItemTreeNode(Item item) {
		super();
		this.item = item;
	}

	/**
	 * Category constructor
	 * @param category
	 * @param childItemCount
	 * @param childCategoryCount
	 */
	public ItemTreeNode(Category category, int childItemCount, int childCategoryCount) {
		super();
		this.category = category;
		this.childItemCount = childItemCount;
		this.childCategoryCount = childCategoryCount;
	}
	
	public boolean isLeaf() {
		return item != null;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getChildItemCount() {
		return childItemCount;
	}

	public int getChildCategoryCount() {
		return childCategoryCount;
	}
	
	@Override
	public String toString() {
		String label = isLeaf() ? item.getName() : category.getName();
		return String.format("[ItemTreeNode %s]", label);
	}
}
