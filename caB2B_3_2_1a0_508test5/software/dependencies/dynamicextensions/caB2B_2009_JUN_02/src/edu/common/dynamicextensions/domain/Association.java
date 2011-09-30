
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;

/**
 * An entity can have multiple associations, where each association is linked to another entity.
 * This Class represents the Association between the Entities.
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_ASSOCIATION" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 * @hibernate.cache  usage="read-write"
 */
public class Association extends AbstractAttribute implements AssociationInterface
{

	/**
	 * Serial Version Unique Identifief
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Direction of the association.
	 */
	protected String direction;

	/**
	 * Source role of association.This specifies how the source entity is related to target entity.
	 */
	protected RoleInterface sourceRole;

	/**
	 * Target role of association.This specifies how the target entity is related to source entity.
	 */
	protected RoleInterface targetRole;

	/**
	 * The target entity of this association.
	 */
	protected EntityInterface targetEntity;

	/**
	 * Constraint properties related to this association.
	 */
	public Collection<ConstraintPropertiesInterface> constraintPropertiesCollection = new HashSet<ConstraintPropertiesInterface>();

	/**
	 * 
	 */
	protected Boolean isSystemGenerated = false;

	/**
	 * Empty Constructor.
	 */
	public Association()
	{
	}

	/**
	 * This method returns the direction of the Association.
	 * @hibernate.property name="direction" type="string" column="DIRECTION" 
	 * @return the direction of the Association
	 */
	private String getDirection()
	{
		return direction;
	}

	/**
	 * This method sets the direction of the Association.
	 * @param direction the direction of the Association to be set.
	 */
	private void setDirection(String direction)
	{

		this.direction = direction;
	}

	/**
	 * @return
	 * @hibernate.many-to-one column="TARGET_ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity" constrained="true"
	 */
	public EntityInterface getTargetEntity()
	{

		return targetEntity;
	}

	/**
	 * This method sets the target Entity of the Association to the given Entity.
	 * @param targetEntity the Entity to be set as target Entity of the Association.
	 */
	public void setTargetEntity(EntityInterface targetEntity)
	{

		this.targetEntity = targetEntity;
	}

	/**
	 * This method returns the source Role of the Association.
	 * @return the source Role of the Association.
	 * @hibernate.many-to-one  cascade="save-update" column="SOURCE_ROLE_ID" class="edu.common.dynamicextensions.domain.Role" constrained="true"
	 */
	public RoleInterface getSourceRole()
	{
		return sourceRole;
	}

	/**
	 * This method sets the source Role of the Association.
	 * @param sourceRole the Role to be set as source Role.
	 */
	public void setSourceRole(RoleInterface sourceRole)
	{
		this.sourceRole = sourceRole;
	}

	/**
	 * This method returns the targetRole of the Association.
	 * @return the targetRole of the Association.
	 * @hibernate.many-to-one cascade="save-update" column="TARGET_ROLE_ID" class="edu.common.dynamicextensions.domain.Role" constrained="true"
	 */

	public RoleInterface getTargetRole()
	{
		return targetRole;
	}

	/**
	 * This method sets the target Role of the Association.
	 * @param targetRole the Role to be set as targetRole of the Association.
	 */
	public void setTargetRole(RoleInterface targetRole)
	{

		this.targetRole = targetRole;
	}

	/**
	 * This method returns the Collection of the ConstraintProperties of the Association.
	 * 
	 * @hibernate.set name="constraintPropertiesCollection" table="DYEXTN_CONSTRAINT_PROPERTIES"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ASSOCIATION_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties" 
	 * 
	 * @return the Collection of the ConstraintProperties of the Association.
	 */
	private Collection<ConstraintPropertiesInterface> getConstraintPropertiesCollection()
	{
		return constraintPropertiesCollection;
	}

	/**
	 * This method sets constraintPropertiesCollection to the given Collection of the ConstraintProperties.
	 * @param constraintPropertiesCollection The constraintPropertiesCollection to set.
	 */
	private void setConstraintPropertiesCollection(
			Collection<ConstraintPropertiesInterface> constraintPropertiesCollection)
	{
		this.constraintPropertiesCollection = constraintPropertiesCollection;
	}

	/**
	 * This method returns the ConstraintProperties of the Association.
	 * @return the ConstraintProperties of the Association. 
	 */
	public ConstraintPropertiesInterface getConstraintProperties()
	{
		ConstraintPropertiesInterface contraintProperties = null;
		if (constraintPropertiesCollection != null && !constraintPropertiesCollection.isEmpty())
		{
			Iterator constraintPropertiesIterator = constraintPropertiesCollection.iterator();
			contraintProperties = (ConstraintPropertiesInterface) constraintPropertiesIterator
					.next();
		}
		return contraintProperties;
	}

	/**
	 * This method sets the constraintProperties to the given ContraintProperties.
	 * @param constraintProperties the constraintProperties to be set.
	 */
	public void setConstraintProperties(ConstraintPropertiesInterface constraintProperties)
	{
		if (constraintPropertiesCollection == null)
		{
			constraintPropertiesCollection = new HashSet<ConstraintPropertiesInterface>();
		}
		else
		{
			constraintPropertiesCollection.clear();
		}
		this.constraintPropertiesCollection.add(constraintProperties);
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#getAssociationDirection()
	 */
	public AssociationDirection getAssociationDirection()
	{
		return AssociationDirection.get(getDirection());
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.AssociationInterface#setAssociationDirection(edu.common.dynamicextensions.util.global.Constants.AssociationDirection)
	 */
	public void setAssociationDirection(AssociationDirection direction)
	{
		setDirection(direction.getValue());
	}

	/**
	 * This method returns whether the Attribute is a Collection or not.
	 * @hibernate.property name="isSystemGenerated" type="boolean" column="IS_SYSTEM_GENERATED" 
	 * @return Returns the isSystemGenerated.
	 */
	public Boolean getIsSystemGenerated()
	{
		return isSystemGenerated;
	}

	/**
	 * @param isSystemGenerated The isSystemGenerated to set.
	 */
	public void setIsSystemGenerated(Boolean isSystemGenerated)
	{
		this.isSystemGenerated = isSystemGenerated;
	}

}