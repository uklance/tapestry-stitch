package org.lazan.t5.stitch.model;

import java.util.List;

public interface LazyTreeModelSource<T> {
	/**
	 * Get the label from the value. Note: for efficiency this should be sourced
	 * directly from the value and should not require an extra database lookup
	 **/
	String getLabel(T value);

	/**
	 * Get the isLeaf flag from the value. Note: for efficiency this should be
	 * sourced directly from the value and should not require an extra database
	 * lookup
	 **/
	boolean isLeaf(T value);

	/**
	 * Get the hasChildren flag from the value. Note: for efficiency this should
	 * be sourced directly from the value and should not require an extra
	 * database lookup
	 **/
	boolean hasChildren(T value);

	/**
	 * Get the children of the value
	 **/
	List<T> getChildren(T value);

	/**
	 * Get the roots of the TreeModel
	 **/
	List<T> getRoots();
}