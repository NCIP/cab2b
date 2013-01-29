/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import java.io.Serializable;


/**
 * This Class represents the a single value for multi select attribute.
 * 
 * @author Rahul Ner 
 * @hibernate.class  table="DE_COLL_ATTR_RECORD_VALUES"
 */
public class CollectionAttributeRecordValue extends DynamicExtensionBaseDomainObject
{

	/**
	 * 
	 */
	protected String value;
	
	
	
	/**
	 * This method returns the name of the AbstractMetadata.
	 * @hibernate.property name="value" type="string" column="RECORD_VALUE" length = "4000"
	 * @return the name of the AbstractMetadata.
	 */
	public String getValue()
	{
		return value;
	}

	
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * This method returns the unique identifier of the AbstractMetadata.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DE_COLL_ATTR_REC_VALUES_SEQ"
	 * @return the identifier of the AbstractMetadata.
	 */
	public Long getId()
	{
		return id;
	}

	
	
}
