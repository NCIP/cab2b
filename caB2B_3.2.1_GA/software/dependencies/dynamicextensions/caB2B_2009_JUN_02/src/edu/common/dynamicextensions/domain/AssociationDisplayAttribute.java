/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.AssociationDisplayAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;

/**
 * @author vishvesh_mulay
 * @hibernate.class table="DYEXTN_ASSO_DISPLAY_ATTR"
 */
public class AssociationDisplayAttribute extends DynamicExtensionBaseDomainObject
		implements
			AssociationDisplayAttributeInterface,
			Comparable
{

	/**
	 * 
	 */
	private int sequenceNumber = 0;

	/**
	 * 
	 */
	private AttributeInterface attribute;

	/**
	 * default constructor.
	 */
	public AssociationDisplayAttribute()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return id identifier
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_ASSO_DISPLAY_ATTR_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param sequenceNumber sequence number
	 */
	public void setSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return sequence number 
	 * @hibernate.property name="sequenceNumber" type="int" column="SEQUENCE_NUMBER"
	 */
	public int getSequenceNumber()
	{
		return sequenceNumber;
	}

	/**
	 * This method returns the source Role of the Association.
	 * @return the source Role of the Association.
	 * @hibernate.many-to-one  cascade="none" column="DISPLAY_ATTRIBUTE_ID" class="edu.common.dynamicextensions.domain.Attribute" constrained="true"
	 */
	public AttributeInterface getAttribute()
	{
		return this.attribute;
	}

	/**
	 * @param attribute attribute
	 */
	public void setAttribute(AttributeInterface attribute)
	{
		this.attribute = attribute;
	}

	/** 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object)
	{
		AssociationDisplayAttribute associationDisplayAttribute = (AssociationDisplayAttribute) object;
		Integer thisSequenceNumber = this.sequenceNumber;
		Integer otherSequenceNumber = associationDisplayAttribute.getSequenceNumber();
		return thisSequenceNumber.compareTo(otherSequenceNumber);
	}
}
