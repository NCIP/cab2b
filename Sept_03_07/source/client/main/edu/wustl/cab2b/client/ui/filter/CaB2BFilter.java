package edu.wustl.cab2b.client.ui.filter;

import java.util.ArrayList;

import org.jdesktop.swingx.decorator.Filter;

/**
 * This class is a CaB2B extension of Filter class
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public abstract class CaB2BFilter extends Filter implements
		CaB2BFilterInterface {

	private ArrayList toPrevious;

	abstract public boolean isRowToBeAdded(int row);

	public CaB2BFilter(int col) {
		super(col);
	}

	@Override
	/**
	 * Get array list size
	 */
	public int getSize() {
		return toPrevious.size();
	}

	@Override
	/**
	 * Overriden abstract method init() implementation to initialize toPrevious
	 */
	protected void init() {
		toPrevious = new ArrayList();

	}

	/**
	 * Overriden abstract method reset() implementation to initialize
	 * fromPrevious
	 */
	protected void reset() {
		toPrevious.clear();
		int inputSize = getInputSize();
		fromPrevious = new int[inputSize];
		for (int i = 0; i < inputSize; i++)
			fromPrevious[i] = -1;

	}

	@Override
	/**
	 * Overriden abstract method filter() implementation
	 */
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
	/**
	 * Returns integer from toPrevious value
	 */
	protected int mapTowardModel(int row) {
		return ((Integer) toPrevious.get(row)).intValue();
	}

}
