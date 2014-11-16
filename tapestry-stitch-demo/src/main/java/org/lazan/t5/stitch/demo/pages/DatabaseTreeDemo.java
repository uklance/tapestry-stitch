package org.lazan.t5.stitch.demo.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.tree.DefaultTreeExpansionModel;
import org.apache.tapestry5.tree.TreeExpansionModel;
import org.apache.tapestry5.tree.TreeModel;
import org.hibernate.Session;
import org.lazan.t5.stitch.demo.entities.Item;
import org.lazan.t5.stitch.demo.model.ItemTreeNode;
import org.lazan.t5.stitch.demo.model.ItemTreeSource;
import org.lazan.t5.stitch.model.LazyTreeModel;

public class DatabaseTreeDemo {
	@Inject
	private Session session;
	
	@Property
	private ItemTreeNode currentNode;
	
	@Property
	private Item selectedItem;
	
	@Inject
	private Block itemBlock;
	
	public TreeModel<ItemTreeNode> getTreeModel() {
		ItemTreeSource source = new ItemTreeSource(session);
		return new LazyTreeModel<ItemTreeNode>(source, source);
	}
	
	public TreeExpansionModel<ItemTreeNode> getExpansionModel() {
		return new DefaultTreeExpansionModel<ItemTreeNode>();
	}
	
	Block onItemSelected(Item item) {
		selectedItem = item;
		return itemBlock;
	}
}
