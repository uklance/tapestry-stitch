package org.lazan.t5.stitch.model;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.tree.TreeNode;

public class LazyTreeNode<T> implements TreeNode<T> {
    private final ValueEncoder<T> encoder;
    private final LazyTreeModelSource<T> source;
    private final T value;
    
    public LazyTreeNode(ValueEncoder<T> encoder, LazyTreeModelSource<T> source, T value) {
        this.encoder = encoder;
        this.source = source;
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    public String getId() {
        return encoder.toClient(value);
    }
    
    public boolean isLeaf() {
        return source.isLeaf(value);
    }
    
    public boolean getHasChildren() {
        return source.hasChildren(value);
    }
    
    public String getLabel() {
        return source.getLabel(value);
    }
    
    public List<TreeNode<T>> getChildren() {
        List<T> children = source.getChildren(value);
        return LazyTreeModel.createTreeNodes(encoder, source, children);
    }
}