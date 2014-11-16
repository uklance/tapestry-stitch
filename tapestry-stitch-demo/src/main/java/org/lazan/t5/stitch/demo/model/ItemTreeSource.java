package org.lazan.t5.stitch.demo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.hibernate.Query;
import org.hibernate.Session;
import org.lazan.t5.stitch.demo.entities.Category;
import org.lazan.t5.stitch.demo.entities.Item;
import org.lazan.t5.stitch.model.LazyTreeModelSource;

/**
 * This service provides the {@link ItemTreeNode}s for a hierarchy of {@link Category}
 * and {@link Item}. Each category may contain sub-categories and sub-items.
 * {@link Item}s have no children and are leaves of the tree.
 * 
 * @author Lance
 */
public class ItemTreeSource
		implements LazyTreeModelSource<ItemTreeNode>, ValueEncoder<ItemTreeNode>
{
	private final Session session;
	
	public ItemTreeSource(Session session) {
		super();
		this.session = session;
	}

	public String getLabel(ItemTreeNode value) {
		if (value.isLeaf()) {
			return value.getItem().getName();
		}
		return value.getCategory().getName();
	}
	
	public boolean hasChildren(ItemTreeNode value) {
		return value.getChildCategoryCount() + value.getChildItemCount() > 0;
	}
	
	/**
	 * Get the children of this node which may contain
	 * {@link Category} and {@link Item} instances.
	 */
	public List<ItemTreeNode> getChildren(ItemTreeNode value) {
		if (value.isLeaf()) {
			return Collections.emptyList();
		}
		Category category = value.getCategory();
		List<ItemTreeNode> children = new ArrayList<ItemTreeNode>();

		// add the categories
		children.addAll(findCategoryNodes(category, null));
		
		// lookup the child items
		for (Item item : category.getItems()) {
			children.add(new ItemTreeNode(item));
		}

		return children;
	}
	
	public boolean isLeaf(ItemTreeNode value) {
		return value.isLeaf();
	}

	public List<ItemTreeNode> getRoots() {
		return findCategoryNodes(null, null);
	}
	
	public String toClient(ItemTreeNode value) {
		if (value.isLeaf()) {
			throw new IllegalStateException("Unexpected toClient call on a leaf node");
		}
		return String.valueOf(value.getCategory().getCategoryId());
	}
	
	public ItemTreeNode toValue(String clientValue) {
		Long categoryId = Long.parseLong(clientValue);
		Category category = (Category) session.get(Category.class, categoryId);
		List<ItemTreeNode> nodes = findCategoryNodes(null,  category);
		if (nodes.size() == 1) {
			return nodes.get(0);
		}
		throw new IllegalStateException(
				String.format("%s nodes found, expecting 1", nodes.size()));
	}
	
	/**
	 * Avoid N+1 selects by doing a single query. The query is a bit complex but it's efficient
	 */
	@SuppressWarnings("unchecked")
	private List<ItemTreeNode> findCategoryNodes(Category parentCategory, Category childCategory) {
		List<ItemTreeNode> children = new ArrayList<ItemTreeNode>();

		// lookup the child categories
		String hqlTemplate = 
			"select childCategory, count(grandChildCategory), count(item) " +
			"from Category childCategory " +
			"left outer join childCategory.childCategories as grandChildCategory " +
			"left outer join childCategory.items as item " +
			"where %s " +
			"group by " + 
				"childCategory.categoryId, childCategory.name, childCategory.parentCategory " +
			"order by childCategory.name";
		
		String whereClause;
		Category param = null;
		if (childCategory != null) {
			whereClause = "childCategory = ?";
			param = childCategory;
		} else if (parentCategory != null) {
			whereClause = "childCategory.parentCategory = ?";
			param = parentCategory;
		} else {
			whereClause = "childCategory.parentCategory is null";
		}
		String hql = String.format(hqlTemplate, whereClause);
		Query query = session.createQuery(hql);
		if (param != null) {
			query.setParameter(0, param);
		}
		List<Object[]> results = query.list();
		for (Object[] row : results) {
			Category current = (Category) row[0];
			int grandChildCategoryCount = ((Number) row[1]).intValue();
			int itemCount = ((Number) row[2]).intValue();
			
			ItemTreeNode treeNode =
					new ItemTreeNode(current, grandChildCategoryCount, itemCount);
			children.add(treeNode);
		}
		return children;
	}
}
