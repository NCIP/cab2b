package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.ICountView;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;


/**
 * @author vijay_pande
 * Class for the Count View of the count query
 */
public class CountView extends ResultView implements ICountView
{
	/**
	 * Entity associated with count view
	 */
	private IQueryEntity countEntity;
	
	/** 
	 * Method to get entity associated with count view
	 * @return countEntity object of type IQueryEntity
	 */
	public IQueryEntity getCountEntity()
	{
		return countEntity;
	}

	/** 
	 * Method to set entity associated with count view
	 * @param countEntity object of type IQueryEntity
	 */
	public void setCountEntity(IQueryEntity countEntity)
	{
		this.countEntity = countEntity;
	}
}
