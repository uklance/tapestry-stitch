package org.lazan.t5.stitch.model;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

/**
 * Ensures that getAvailableRows() is only called once
 * @author Lance
 */
public class CachedSizeGridDataSource implements GridDataSource {
	private final GridDataSource delegate;
	private final int availableRows;

	public CachedSizeGridDataSource(GridDataSource delegate) {
		super();
		this.delegate = delegate;
		this.availableRows = delegate.getAvailableRows();
	}

	public int getAvailableRows() {
		return availableRows;
	}

	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
		delegate.prepare(startIndex, endIndex, sortConstraints);
	}

	public Object getRowValue(int index) {
		return delegate.getRowValue(index);
	}

	public Class getRowType() {
		return delegate.getRowType();
	}
}
