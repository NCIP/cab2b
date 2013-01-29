/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single record for an entity
 * 
 * @author Rahul Ner
 * @author vishvesh Mulay
 */
public class EntityRecord implements EntityRecordInterface,Serializable 
{

    /**
     * Serial Version Unique Identifier
     */
    private static final long serialVersionUID = -552600540977483821L;

    /**
	 * 
	 */
	Long recordId;
	
	/**
	 * 
	 */
	List recordValueList = new ArrayList();
	
	/**
	 *
	 */
	public EntityRecord()
	{
		
		super();
		
	}
	/**
	 * @param id id
	 */
	public EntityRecord(Long id)
	{
		recordId = id;
	}


	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordInterface#getRecordId()
	 */
	public Long getRecordId()
	{
		return recordId;
	}
	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordInterface#setRecordId(java.lang.Long)
	 */
	public void setRecordId(Long recordId)
	{
		this.recordId = recordId;
	}


	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordInterface#getRecordValueList()
	 */
	public List getRecordValueList()
	{
		return recordValueList;
	}


	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordInterface#setRecordValueList(java.util.List)
	 */
	public void setRecordValueList(List recordValueList)
	{
		this.recordValueList = recordValueList;
	}
	
	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityRecordInterface#addRecordValue(java.lang.Object)
	 */
	public void addRecordValue(int index,Object value)
	{
		this.recordValueList.add(index,value);
		
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return recordValueList.toString();
	}
	
	
}
