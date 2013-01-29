/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.ICompositeQuery;
import edu.wustl.common.querysuite.queryobject.IOperation;
import edu.wustl.common.util.Identifiable;


/**
 * @author vijay_pande
 * Class for composite query
 */
public class CompositeQuery extends AbstractQuery implements ICompositeQuery, Identifiable
{
	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * operation associated with the composite query
	 */
	private IOperation operation;

	
	/**
	 * Method to get operation associated with the composite query
	 * @return operation Object of type IOperation 
	 */
	public IOperation getOperation()
	{
		return operation;
	}

	/**
	 * Method to set operation associated with the composite query
	 * @param operation Object of type IOperation 
	 */
	public void setOperation(IOperation operation)
	{
		this.operation = operation;
	}
	
	/**
	 * Method to get object id of the composite query required while saving CSM data
	 * @param objectid  Object of type String
	 */
	public String getObjectId() 
	{
		return this.getClass().getName()+ "_" + this.getId();
	}
}
