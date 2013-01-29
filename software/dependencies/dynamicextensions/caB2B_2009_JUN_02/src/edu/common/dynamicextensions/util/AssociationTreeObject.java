/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.util;

import java.util.Collection;
import java.util.HashSet;


public class AssociationTreeObject
{
	/**
	 * 
	 */
	Long id;
	
	/**
	 * 
	 */
	
	String label;
	/**
	 * 
	 * @param id
	 * @param label
	 */
	public AssociationTreeObject(Long id,String label)
	{
		this.id = id;
		this.label = label;
	}
	/**
	 * 
	 *
	 */
	public AssociationTreeObject()
	{
	
	}
	
	/**
	 * 
	 */
	Collection associationTreeObjectCollection;
	/**
	 * 
	 * @return
	 */
	public Collection getAssociationTreeObjectCollection()
	{
		return associationTreeObjectCollection;
	}
	
	/**
	 * 
	 * @param associationTreeObjectCollection
	 */
	public void setAssociationTreeObjectCollection(Collection associationTreeObjectCollection)
	{
		this.associationTreeObjectCollection = associationTreeObjectCollection;
	}
	
	/**
	 * 
	 * @param associationTreeObject
	 */
	public void addAssociationTreeObject(AssociationTreeObject associationTreeObject)
	{
		if(this.associationTreeObjectCollection == null)
		{
			associationTreeObjectCollection = new HashSet();
		}
		associationTreeObjectCollection.add(associationTreeObject);
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getId()
	{
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel()
	{
		return label;
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label)
	{
		this.label = label;
	} 

}
