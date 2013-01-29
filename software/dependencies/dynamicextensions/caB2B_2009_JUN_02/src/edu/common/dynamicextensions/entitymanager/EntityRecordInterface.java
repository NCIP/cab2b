/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.List;

/**
 * This is an interface to operate on the entity record 
 * @author Rahul Ner
 * @author vishvesh Mulay
 *
 */
public interface EntityRecordInterface
{

	/**
	 * @return the recordId
	 */
	Long getRecordId();

	/**
	 * @param recordId the recordId to set
	 */
	void setRecordId(Long recordId);

	/**
	 * @return List list of record values
	 */
	List getRecordValueList();

	/**
	 * @param recordValueList list of record values
	 */
	void setRecordValueList(List recordValueList);

	
	/**
	 * @param index index
	 * @param value value
	 */
	void addRecordValue(int index,Object value);
}