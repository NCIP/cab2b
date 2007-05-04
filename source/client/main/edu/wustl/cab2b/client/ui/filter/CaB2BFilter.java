/**
 * <p>Title: CaB2BFilter Class>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 * @version 1.0
 */

package edu.wustl.cab2b.client.ui.filter;

import java.util.ArrayList;

import org.jdesktop.swingx.decorator.Filter;

/**
 * This class is a CaB2B extension of Filter class
 * */

public abstract class CaB2BFilter extends Filter implements
		CaB2BFilterInterface {

	private ArrayList toPrevious;
	
	abstract public boolean isRowToBeAdded(int row);
	
	
	public CaB2BFilter(int col) {
		super(col);
	}

	

	@Override
	public int getSize() {
		return toPrevious.size();
	}

	@Override
	protected void init() {
		toPrevious = new ArrayList();

	}

	/**
	 * @see org.jdesktop.swingx.decorator.Filter#reset()
	 */
	protected void reset() {
		toPrevious.clear();
		int inputSize = getInputSize();
		fromPrevious = new int[inputSize];
		for (int i = 0; i < inputSize; i++)
			fromPrevious[i] = -1;

	}

	@Override
	protected void filter() {
		int inputSize = getInputSize();
		int current = 0;
		for (int i = 0; i < inputSize; i++)
			if (isRowToBeAdded(i)) {
				toPrevious.add(new Integer(i));
				fromPrevious[i] = current++;
			}

	}

	

	@Override
	protected int mapTowardModel(int row) {
		return ((Integer) toPrevious.get(row)).intValue();
	}

}
