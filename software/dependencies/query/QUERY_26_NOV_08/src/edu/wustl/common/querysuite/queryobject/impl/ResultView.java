/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IResultView;


/**
 * @author vijay_pande
 * Base class for result view of query
 */
public abstract class ResultView implements IResultView
{
	/**
	 * identifier of result view
	 */
	protected Long id;

	
	/**
	 * Method to get identifier of the operation
	 * @return id of type Long
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Method to set identifier of the operation
	 * @param id of type Long
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
}
