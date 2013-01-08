/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;


/**
 * @author vijay_pande
 * Interface to declare operations for composite query operation
 */
public interface IOperation
{
	/**
	 * Method to get id of the operation
	 * @return id identifier object of Long
	 */
	public Long getId();
	
	/**
	 * Method to get first operand for operation
	 * @return query object of type IAbstractQuery
	 */
	public IAbstractQuery getOperandOne();
	
	/**
	 * Method to get second operand for operation
	 * @return query object of type IAbstractQuery
	 */
	public IAbstractQuery getOperandTwo();
	
	/**
	 * Method to set id of the operation
	 * @param id identifier object of Long
	 */
	public void setId(Long id);
	
	/**
	 * Method to set first operand for operation
	 * @param query object of type IAbstractQuery
	 */
	public void setOperandOne(IAbstractQuery operandOne);
	
	/**
	 * Method to set second operand for operation
	 * @param query object of type IAbstractQuery
	 */
	public void setOperandTwo(IAbstractQuery operandTwo);	
}
