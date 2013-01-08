/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.util;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.validationrules.RuleParameterInterface;

/**
 * 
 * @author deepti_shelar
 *
 */
public class RuleConfigurationObject
{
	private String displayLabel;
	private String ruleName;
	private String ruleClassName;
	private String errorKey;
	private List<RuleParameterInterface> ruleParametersList = null;

	/**
	 * @return the displayLabel
	 */
	public String getDisplayLabel()
	{
		return displayLabel;
	}

	/**
	 * @param displayLabel the displayLabel to set
	 */
	public void setDisplayLabel(String displayLabel)
	{
		this.displayLabel = displayLabel;
	}

	/**
	 * @return the errorKey
	 */
	public String getErrorKey()
	{
		return errorKey;
	}

	/**
	 * @param errorKey the errorKey to set
	 */
	public void setErrorKey(String errorKey)
	{
		this.errorKey = errorKey;
	}

	/**
	 * @return the ruleClassName
	 */
	public String getRuleClassName()
	{
		return ruleClassName;
	}

	/**
	 * @param ruleClassName the ruleClassName to set
	 */
	public void setRuleClassName(String ruleClassName)
	{
		this.ruleClassName = ruleClassName;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName()
	{
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName)
	{
		this.ruleName = ruleName;
	}

	/**
	 * @return the ruleParametersList
	 */
	public List<RuleParameterInterface> getRuleParametersList()
	{
		return ruleParametersList;
	}

	/**
	 * @param ruleParametersList the ruleParametersList to set
	 */
	public void setRuleParametersList(List<RuleParameterInterface> ruleParametersList)
	{
		this.ruleParametersList = ruleParametersList;
	}

}
