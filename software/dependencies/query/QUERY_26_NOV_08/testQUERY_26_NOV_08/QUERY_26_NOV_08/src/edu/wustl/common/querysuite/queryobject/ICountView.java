/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;


/**
 * @author vijay_pande
 * Interface for the CountView. The result view related to Count Query. 
 */
public interface ICountView extends IResultView
{
	/**
	 * Method to return CountEntity. CountEntity is the entity for which the count is to be generated.
	 * @return countEntity Object of type IQueryEntity
	 */
	public IQueryEntity getCountEntity();
	/**
	 * Method to set CountEntity. CountEntity is the entity for which the count is to be generated.
	 * @param countEntity Object of type IQueryEntity
	 */
	public void setCountEntity(IQueryEntity countEntity);
}
