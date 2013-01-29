/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;


/**
 * @author vijay_pande
 * Interface for Composite Query
 */
public interface ICompositeQuery extends IAbstractQuery
{
	/**
	 * Method to return operation associated with the Composite query
	 * @return operation Object of type IOperation
	 */
	public IOperation getOperation();
	
	/**
	 * Method to set operation associated with the Composite query
	 * @param operation Object of type IOperation
	 */
	public void setOperation(IOperation operation);
}
