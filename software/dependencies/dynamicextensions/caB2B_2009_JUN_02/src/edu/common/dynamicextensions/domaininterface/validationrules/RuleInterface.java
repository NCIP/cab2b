/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface.validationrules;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;

/**
 * Rules are the validations put by the end user on the Attributes of the Enitites they create.
 * @author sujay_narkar
 * @version 1.0
 */
public interface RuleInterface extends DynamicExtensionBaseDomainObjectInterface
{

	/**
	 * This method returns the Unique Identifier of the Object.
	 * @return the Unique Identifier of the Object.
	 */
	public Long getId();

	/**
	 * This method returns the name of the Rule.
	 * @return the name of the Rule.
	 */
	public String getName();

	/**
	 * This method sets the name of the Rule.
	 * @param name the name to be set.
	 */
	public void setName(String name);

	/**
	 * This method returns the Collection of RuleParameters.
	 * @return the Collection of RuleParameters.
	 */
	public Collection<RuleParameterInterface> getRuleParameterCollection();

	/**
	 * This method sets ruleParameterCollection to the Collection of RuleParmeters.
	 * @param ruleParameterCollection the the Collection of RuleParmeters to be set.
	 */
	public void setRuleParameterCollection(Collection<RuleParameterInterface> ruleParameterCollection);

}
