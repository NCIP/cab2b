/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

public class HQLPlaceHolderObject
{
	/**
	 * 
	 */
	String type;
	/**
	 * 
	 */
	Object value;
	/**
	 * 
	 *
	 */
	public HQLPlaceHolderObject()
	{
		
	}
	/**
	 * 
	 * @param type
	 * @param value
	 */
	public HQLPlaceHolderObject(String type,Object value)
	{
		this.type = type;
		this.value = value;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * 
	 * @param type
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	/**
	 * 
	 * @return
	 */
	public Object getValue()
	{
		return value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}
	
	
	
	

}
