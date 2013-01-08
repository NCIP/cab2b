/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */ 
package edu.common.dynamicextensions.domaininterface;



public interface AssociationDisplayAttributeInterface
		extends
			DynamicExtensionBaseDomainObjectInterface
{
	/**
	 * @param sequenceNumber sequence number
	 */
	void setSequenceNumber(int sequenceNumber);


	/**
	 * @return sequence number 
	 */
	int getSequenceNumber();


	/**
	 * This method returns the source Role of the Association.
	 * @return the source Role of the Association.
	 * @hibernate.many-to-one  cascade="none" column="DISPLAY_ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.Attribute" constrained="true"
	 */
	AttributeInterface getAttribute();

	/**
	 * @param attribute attribute
	 */
	void setAttribute(AttributeInterface attribute);
	

}
