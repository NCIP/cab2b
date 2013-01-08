/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.CaDSRValueDomainInfoInterface;
import edu.common.dynamicextensions.util.global.Constants.ValueDomainType;


/**
 * This class stores value domain information of each attribute.
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_CADSR_VALUE_DOMAIN_INFO"
 */
public class CaDSRValueDomainInfo extends DynamicExtensionBaseDomainObject implements CaDSRValueDomainInfoInterface
{
	/**
	 * 
	 */
	String name;
	/**
	 * 
	 */
	String datatype;
	/**
	 * 
	 */
	String type;
	
	/**
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_VALUE_DOMAIN_SEQ" 
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}
	
	/**
	 * 
	 * @hibernate.property name="datatype" type="string" column="DATATYPE" 
	 * @return the datatype
	 */
	public String getDatatype()
	{
		return datatype;
	}
	
	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatype(String datatype)
	{
		this.datatype = datatype;
	}
	
	/**
	 * @hibernate.property name="name" type="string" column="NAME" 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @hibernate.property name="type" type="string" column="TYPE"  
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getAssociationDirection()
	 */
	public ValueDomainType getValueDomainType()
	{
		return ValueDomainType.get(getType());
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setAssociationDirection(edu.common.dynamicextensions.util.global.Constants.AssociationDirection)
	 */
	public void setValueDomainType(ValueDomainType valueDomainType)
	{
		setType(valueDomainType.getValue());
	}

}
