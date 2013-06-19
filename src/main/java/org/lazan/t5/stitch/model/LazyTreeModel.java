package org.lazan.t5.stitch.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.tree.TreeModel;
import org.apache.tapestry5.tree.TreeNode;

public class LazyTreeModel<T> implements TreeModel<T> {
	private final ValueEncoder<T> encoder;
	private final LazyTreeModelSource<T> source;

	public LazyTreeModel(ValueEncoder<T> encoder, LazyTreeModelSource<T> source) {
		this.encoder = encoder;
		this.source = source;
	}

	public List<TreeNode<T>> getRootNodes() {
		List<T> roots = source.getRoots();
		return LazyTreeModel.createTreeNodes(encoder, source, roots);
	}

	public TreeNode<T> getById(String id) {
		return find(encoder.toValue(id));
	}

	public TreeNode<T> find(T value) {
		return new LazyTreeNode<T>(encoder, source, value);
	}

	public static <T> List<TreeNode<T>> createTreeNodes(ValueEncoder<T> encoder, LazyTreeModelSource<T> source, List<T> siblings) {
		if (siblings == null || siblings.isEmpty()) {
			return Collections.emptyList();
		}
		List<TreeNode<T>> treeNodes = new ArrayList<TreeNode<T>>(siblings.size());
		for (T value : siblings) {
			treeNodes.add(new LazyTreeNode<T>(encoder, source, value));
		}
		return treeNodes;
	}
}