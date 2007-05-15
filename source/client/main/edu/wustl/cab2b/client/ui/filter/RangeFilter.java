package edu.wustl.cab2b.client.ui.filter;

/**
 * This class implements a filter of range type that filters out all the rows
 * that are out of bound of the specified minimum and maximum range values by
 * user.
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class RangeFilter extends CaB2BFilter {
	float minRange;

	float maxRange;
	
	float originalMin;
	
	float originalMax;
	
	String columnName;

	public RangeFilter(float minRange, float maxRange, int col, String colName, float min, float max) {
		super(col);
		this.columnName=colName;
		this.minRange = minRange;
		this.maxRange = maxRange;
		originalMin=min;
		originalMax=max;
	}

	public void rangeValidation() {
		if (minRange > maxRange) {
			float temp = 0;
			temp = minRange;
			minRange = maxRange;
			maxRange = temp;
		}
	}

	public boolean isRowToBeAdded(int row) {
		rangeValidation();
		if (!adapter.isTestable(getColumnIndex()))
			return false;
		Object value = getInputValue(row, getColumnIndex());
		if (value == null) {
			return false;
		} else {
			float valueInt = Float.parseFloat(value.toString());

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
		return new RangeFilter(this.minRange, this.maxRange, this.getColumnIndex(), this.columnName, this.originalMin,this.originalMax);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(minRange);
		stringBuffer.append(" < ");
		stringBuffer.append("\"");
		stringBuffer.append(columnName);
		stringBuffer.append("\"");
		stringBuffer.append(" < ");
		stringBuffer.append(maxRange);
		
		return(stringBuffer.toString());
	}
}
