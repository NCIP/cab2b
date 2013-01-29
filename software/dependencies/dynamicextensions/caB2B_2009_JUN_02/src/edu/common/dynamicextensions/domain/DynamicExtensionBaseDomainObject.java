/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * This is base domain object for all the dynamic extension domain object
 * @author Rahul Ner
 */
public abstract class DynamicExtensionBaseDomainObject extends AbstractDomainObject
		implements DynamicExtensionBaseDomainObjectInterface
{

	/**
	 * Serial Version Unique Identifief
	 */
	protected static final long serialVersionUID = 1234567890L;

	/**
	 * Internally generated identifier.
	 */
	protected Long id;

	/** 
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException
	{

	}
    
    public void setAllValues(IValueObject arg0) throws AssignDataException
    {

    }
    
    

	/** 
	 * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * This method returns the System Identifier of the AbstractMetadata.
	 * @return Long the unique System Identifier of the AbstractMetadata.
	 */
	public Long getSystemIdentifier()
	{
		return id;
	}

	/**
	 * This method sets the unique System Identifier of the AbstractMetadata.
	 * @param systemIdentifier the System Identifier to be set.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.id = systemIdentifier;
	}
	
	/**
	 * This method overrides the equals method of the Object Class.
	 * This method checks the equality of the AbstractMetadata objects.
	 * @return boolean true if the both AbstractMetadata objects are equal otherwise false. 
	 */
	public boolean equals(Object obj)
	{
		boolean equals = false;
		if (obj instanceof DynamicExtensionBaseDomainObject)
		{
			DynamicExtensionBaseDomainObject object = (DynamicExtensionBaseDomainObject) obj;
			Long thisId = getId();

			if (thisId != null && thisId.equals(object.getId()))
			{
				equals = true;
			}
		}
		return equals;
	}
	
	/**
	 * This method overrides the equals method of the Object Class.
	 * It returns the HashCode of this AttributeMetadata instance.
	 * @return int The HashCode of the AttributeMetadata instance.
	 */
	public int hashCode()
	{

		return 1;
//				int hashCode = 0;
//
//		if (getId() != null)
//		{
//			hashCode += getId().hashCode();
//		}
//		return hashCode;
	}
}
