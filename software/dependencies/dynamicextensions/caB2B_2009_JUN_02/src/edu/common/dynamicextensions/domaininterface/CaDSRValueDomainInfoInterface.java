/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.util.global.Constants.ValueDomainType;


public interface CaDSRValueDomainInfoInterface extends DynamicExtensionBaseDomainObjectInterface
{
	/**
	 * @return Returns the id.
	 */
	public Long getId();
	
	/**
	 * @return the datatype
	 */
	public String getDatatype();
		
	/**
	 * @param datatype the datatype to set
	 */
	public void setDatatype(String datatype);
		
	/**
	 * @return the name
	 */
	public String getName();
		
	/**
	 * @param name the name to set
	 */
	public void setName(String name);
	
	/**
	 * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getAssociationDirection()
	 */
	public ValueDomainType getValueDomainType();
	
	/**
	 * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setAssociationDirection(edu.common.dynamicextensions.util.global.Constants.AssociationDirection)
	 */
	public void setValueDomainType(ValueDomainType valueDomainType);
	
}
