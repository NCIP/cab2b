/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface.validationrules;

import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;

/**
 * Rules are the validations put by the end user on the Attributes of the Enitites they create.
 * @author sujay_narkar
 * @version 1.0
 */
public interface RuleParameterInterface extends DynamicExtensionBaseDomainObjectInterface
{
	/**
	 * This method returns the Unique Identifier of the Object.
	 * @return the Unique Identifier of the Object.
	 */
	public Long getId();

	/**
	 * This method returns the name of the parameter of the Rule.
	 * @return the name of the Rule.
	 */
	public String getName();

	/**
	 * This method sets the name of the parameter of the Rule.
	 * @param name the name to be set.
	 */
	public void setName(String name);

	/**
	 * This method returns the value of the parameter of the Rule.
	 * @return the value of the parameter of the Rule.
	 */
	public String getValue();

	/**
	 * This method sets the value of the parameter of the Rule.
	 * @return the value of the parameter to be set.
	 */
	public void setValue(String value);

}
