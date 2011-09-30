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
 * This is an interface to operate on the entity record result object.
 * @author Rahul Ner
 * @author vishvesh Mulay
 *
 */
public interface EntityRecordResultInterface
{

	/**
	 * @return Returns the entityRecordList.
	 */
	List<EntityRecordInterface> getEntityRecordList();

	/**
	 * @param entityRecordList The entityRecordList to set.
	 */
	void setEntityRecordList(List<EntityRecordInterface> entityRecordList);

	/**
	 * @return Returns the entityRecordMetadata.
	 */
	EntityRecordMetadata getEntityRecordMetadata();

	/**
	 * @param entityRecordMetadata The entityRecordMetadata to set.
	 */
	void setEntityRecordMetadata(EntityRecordMetadata entityRecordMetadata);

}