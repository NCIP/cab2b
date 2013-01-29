/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;

/**
 * These are the attributes in the entities.
 * @author geetika_bangard
 */
public interface AttributeInterface extends AbstractAttributeInterface
{

	/**
	 * This method returns whether the Attribute is a Collection or not.
	 * @return whether the Attribute is a Collection or not.
	 */
	Boolean getIsCollection();

	/**
	 * This method sets whether the Attribute is a Collection or not.
	 * @param isCollection the Boolean value to be set.
	 */
	void setIsCollection(Boolean isCollection);

	/**
	 * This method returns whether the Attribute is identifiable or not.
	 * @return whether the Attribute is identifiable or not.
	 */
	Boolean getIsIdentified();

	/**
	 * This method sets whether the Attribute is identifiable or not.
	 * @param isIdentified the Boolean value to be set.
	 */
	void setIsIdentified(Boolean isIdentified);

	/**
	 * This method retunrs whehter the Attribute is a primary key or not.
	 * @return whehter the Attribute is a primary key or not.
	 */
	Boolean getIsPrimaryKey();

	/**
	 * This method sets whehter the Attribute is a primary key or not.
	 * @param isPrimaryKey the Boolean value to be set.
	 */
	void setIsPrimaryKey(Boolean isPrimaryKey);

	/**
	 * This method retunrs whehter the Attribute is a nullable or not.
	 * @return whehter the Attribute is a nullable or not.
	 */
	Boolean getIsNullable();

	/**
	 * This method sets whehter the Attribute is a nullable or not.
	 * @param isNullable the Boolean value to be set.
	 */
	void setIsNullable(Boolean isNullable);

	/**
	 * Column properties object contains database column name  
	 * @return ColumnPropertiesInterface ColumnPropertiesInterface
	 */
	ColumnPropertiesInterface getColumnProperties();

	/**
	 * @param columnProperties
	 */
	void setColumnProperties(ColumnPropertiesInterface columnProperties);

	/**
	 * 
	 * @return AttributeTypeInformationInterface
	 */
	AttributeTypeInformationInterface getAttributeTypeInformation();

	/**
	 * @param attributeTypeInformationInterface attributeTypeInformationInterface
	 */
	void setAttributeTypeInformation(
			AttributeTypeInformationInterface attributeTypeInformationInterface);
	
	
	/**
	 * The method returns the attribute type of the attribute based on it's attributeTypeInformation object.
	 * @return Strin attribute type
	 */
	String getDataType();
	
	/**
	 * 
	 * @return AttributeTypeInformationInterface
	 */
	public CaDSRValueDomainInfoInterface getCaDSRValueDomainInfo();
	
	/**
	 * 
	 * @return AttributeTypeInformationInterface
	 */
	public void setCaDSRValueDomainInfo(CaDSRValueDomainInfoInterface caDSRValueDomainInfoInterface);
}
