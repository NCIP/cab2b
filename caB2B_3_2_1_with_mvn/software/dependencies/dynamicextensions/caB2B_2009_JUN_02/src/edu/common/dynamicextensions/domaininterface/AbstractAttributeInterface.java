
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 * This interface is extended by AssociationInterface and PrimitiveAttributeInterface.
 * Associations are also treated as attributes.  
 *      Using the information of Attribute object coulmns are perpared in the dynamically create tables.
 * @author geetika_bangard
 */
public interface AbstractAttributeInterface extends AbstractMetadataInterface
{

	/**
	 * This method returns the Collection of rules.
	 * @return Collection the ruleCollection associated with the Attribute.
	 */
	Collection<RuleInterface> getRuleCollection();

	/**
	 * This method sets the ruleCollection field member to given rule Collection.
	 * @param ruleCollection The ruleCollection to set.
	 */
	void setRuleCollection(Collection<RuleInterface> ruleCollection);

	/**
	 * This method adds a rule to this Attribute.
	 * @param ruleInterface A Rule instance
	 */
	void addRule(RuleInterface ruleInterface);

	/**
	 * This method removes the Rule form the Collection of Rules of this Attribute. 
	 * ruleInterface the Rule instance to be removed
	 */
	void removeRule(RuleInterface ruleInterface);

	/**
	 * This method returns the Entity associated with this Attribute.
	 * @return EntityInterface the Entity associated with the Attribute.
	 */
	EntityInterface getEntity();

	/**
	 * This method sets the Entity associated with this Attribute.
	 * @param entityInterface The entity to be set.
	 */
	void setEntity(EntityInterface entityInterface);

}
