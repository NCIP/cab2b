/**
 * <p>Title: RangeFilter Class>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 * @version 1.0
 */
package edu.wustl.cab2b.client.ui.filter;

/**
 * This class implements a filter of range type that filters out all the rows
 * that are out of bound of the specified minimum and maximum range values by
 * user.
 */
public class RangeFilter extends CaB2BFilter {
	int minRange;

	int maxRange;

	public RangeFilter(int minRange, int maxRange, int col) {
		super(col);
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	public boolean isRowToBeAdded(int row) {
		if (!adapter.isTestable(getColumnIndex()))
			return false;
		Object value = getInputValue(row, getColumnIndex());
		if (value == null) {
			return false;
		} else {
			int valueInt = Integer.parseInt(value.toString());

			if (valueInt >= minRange && valueInt <= maxRange)
				return true;
			else
				return false;
		}
	}

	public void setMinMaxValues(int minRange, int maxRange) {
		this.minRange = minRange;
		this.maxRange = maxRange;
		fireFilterChanged();
	}

	public CaB2BFilterInterface copy() {
		return new RangeFilter(this.minRange, this.maxRange, this
				.getColumnIndex());
	}
}
