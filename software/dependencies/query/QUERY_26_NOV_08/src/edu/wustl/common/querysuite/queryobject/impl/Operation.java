/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject.impl;

import edu.wustl.common.querysuite.queryobject.IAbstractQuery;
import edu.wustl.common.querysuite.queryobject.IOperation;


/**
 * @author vijay_pande
 * Base class for operation to be performed for composite query
 */
public abstract class Operation implements IOperation
{
	/**
	 * identifier of the operation
	 */
	protected Long id;
	
	/**
	 * first operand of the operation
	 */
	protected IAbstractQuery operandOne;
	
	/**
	 * second operand of the operation
	 */
	protected IAbstractQuery operandTwo;
	
	
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
	
	/**
	 * Method to get first operand of the operation
	 * @return operandOne of type AbstractQuery
	 */
	public IAbstractQuery getOperandOne()
	{
		return operandOne;
	}
	
	/**
	 * Method to set first operand of the operation
	 * @param operandOne of type AbstractQuery
	 */
	public void setOperandOne(IAbstractQuery operandOne)
	{
		this.operandOne = operandOne;
	}
	
	/**
	 * Method to get second operand of the operation
	 * @return operandTwo of type AbstractQuery
	 */
	public IAbstractQuery getOperandTwo()
	{
		return operandTwo;
	}
	
	/**
	 * Method to set second operand of the operation
	 * @param operandTwo of type AbstractQuery
	 */
	public void setOperandTwo(IAbstractQuery operandTwo)
	{
		this.operandTwo = operandTwo;
	}
	
}
