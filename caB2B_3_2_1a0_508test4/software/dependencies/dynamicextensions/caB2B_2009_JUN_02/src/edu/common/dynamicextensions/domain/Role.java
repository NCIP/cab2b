
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Class represents the role of the Association.
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.class table="DYEXTN_ROLE"
 * @hibernate.cache  usage="read-write"
 */
public class Role extends DynamicExtensionBaseDomainObject implements RoleInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 8674217047857771139L;

	/**
	 * The association type : containment or linking
	 */
	protected String associationType;

	/**
	 *  Maximum cardinality of this role.
	 */
	protected Integer maxCardinality;

	/**
	 * Minimum cardinality of this role.
	 */
	protected Integer minCardinality;

	/**
	 * Name of the role.
	 */
	protected String name;

	/**
	 * Empty constructor.
	 */
	public Role()
	{
	}

	/**
	 * This method returns the Unique identifier of this Object.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_ROLE_SEQ"
	 * @return the Unique identifier of this Object.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method returns the type of Association.
	 * @hibernate.property name="associationType" type="string" column="ASSOCIATION_TYPE"
	 * @return the type of Association. 
	 */
	private String getAssociationType()
	{
		return associationType;
	}

	/**
	 * This method sets the type of Association. 
	 * @param associationType the type of Association to be set.
	 */
	private void setAssociationType(String associationType)
	{
		this.associationType = associationType;
	}

	/**
	 * This method returns the maximum cardinality.
	 * @hibernate.property name="maxCardinality" type="integer" column="MAX_CARDINALITY" 
	 * @return the maximum cardinality.
	 */
	public Integer getMaxCardinality()
	{
		return maxCardinality;
	}

	/**
	 * This method sets the maximum cardinality.
	 * @param maxCardinality the value to be set as maximum cardinality.
	 */
	public void setMaxCardinality(Integer maxCardinality)
	{
		this.maxCardinality = maxCardinality;
	}

	/**
	 * This method returns the minimum cardinality.
	 * @hibernate.property name="minCardinality" type="integer" column="MIN_CARDINALITY" 
	 * @return Returns the minimum cardinality.
	 */
	public Integer getMinCardinality()
	{
		return minCardinality;
	}

	/**
	 * This method sets the minimum cardinality.
	 * @param minCardinality the value to be set as minimum cardinality.
	 */
	public void setMinCardinality(Integer minCardinality)
	{
		this.minCardinality = minCardinality;
	}

	/**
	 * This method returns the name of the role.
	 * @hibernate.property name="name" type="string" column="NAME" 
	 * @return the name of the role.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * This method sets the name of the role.
	 * @param name the name to be set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * @see edu.common.dynamicextensions.domaininterface.RoleInterface#getMaximumCardinality()
	 */
	public Cardinality getMaximumCardinality()
	{
		return Cardinality.get(getMaxCardinality());
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.RoleInterface#setMaximumCardinality(edu.common.dynamicextensions.util.global.Constants.Cardinality)
	 */
	public void setMaximumCardinality(Cardinality maxCardinality)
	{
		setMaxCardinality(maxCardinality.getValue());

	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.RoleInterface#getMinimumCardinality()
	 */
	public Cardinality getMinimumCardinality()
	{
		return Cardinality.get(getMinCardinality());
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.RoleInterface#setMinimumCardinality(edu.common.dynamicextensions.util.global.Constants.Cardinality)
	 */
	public void setMinimumCardinality(Cardinality minCardinality)
	{
		setMinCardinality(minCardinality.getValue());
	}


	/**
	 * @see edu.common.dynamicextensions.domaininterface.RoleInterface#getAssociationsType()
	 */
	public AssociationType getAssociationsType()
	{
		return AssociationType.get(getAssociationType());
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.RoleInterface#setAssociationsType(edu.common.dynamicextensions.util.global.Constants.AssociationType)
	 */
	public void setAssociationsType(AssociationType associationType)
	{
		 setAssociationType(associationType.getValue());
	}

}