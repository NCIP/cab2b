/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 * This Class represents the general Attribute of the Entities
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @hibernate.cache  usage="read-write"
 */
public abstract class AbstractAttribute extends AbstractMetadata implements AbstractAttributeInterface
{
	/**
	 * Serial Version Unique Identifier
	 */
	protected static final long serialVersionUID = 1234567890L;

	/**
	 * Entity to which this Attribute belongs
	 */
	protected Entity entity;

	/**
	 * Collection of rules.
	 */
	protected Collection<RuleInterface> ruleCollection = new HashSet<RuleInterface>();

	/**
	 * Empty constructor
	 */
	public AbstractAttribute()
	{
	}

	/**
	 * This method returns the Collection of rules.
	 * @hibernate.set name="ruleCollection" table="DYEXTN_RULE"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ATTRIBUTE_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.validationrules.Rule"
	 * @return Collection the ruleCollection associated with the Attribute.
	 */
	public Collection<RuleInterface> getRuleCollection()
	{
		return ruleCollection;
	}

	/**
	 * This method sets the ruleCollection field member to given rule Collection.
	 * @param ruleCollection The ruleCollection to set.
	 */
	public void setRuleCollection(Collection<RuleInterface> ruleCollection)
	{
		this.ruleCollection = ruleCollection;
	}

	/**
	 * This method adds a rule to this Attribute.
	 * @param ruleInterface A Rule instance
	 */
	public void addRule(RuleInterface ruleInterface)
	{
		if (ruleCollection == null)
		{
			ruleCollection = new HashSet<RuleInterface>();
		}
		ruleCollection.add(ruleInterface);
	}

	/**
	 * This method removes the Rule form the Collection of Rules of this Attribute. 
	 * ruleInterface the Rule instance to be removed
	 */
	public void removeRule(RuleInterface ruleInterface)
	{
		if (ruleCollection != null && ruleCollection.contains(ruleInterface))
		{
			ruleCollection.remove(ruleInterface);
		}
	}

	/**
	 * This method returns the Entity associated with this Attribute.
	 * @hibernate.many-to-one column="ENTIY_ID" class="edu.common.dynamicextensions.domain.Entity" constrained="true"
	 * @return EntityInterface the Entity associated with the Attribute.
	 */
	public EntityInterface getEntity()
	{
		return entity;
	}

	/**
	 * This method sets the Entity associated with this Attribute.
	 * @param entityInterface The entity to be set.
	 */
	public void setEntity(EntityInterface entityInterface)
	{
		if (entityInterface != null)
		{
			this.entity = (Entity)entityInterface;
			//TODO //entityInterface.addAbstractAttribute(this);
		}

	}

}