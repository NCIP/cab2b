/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * Created on Nov 22, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.util;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OptionValueObject
{
	private String optionName = null;
	private String optionDescription = null;
	private String optionConceptCode = null;
	public String getOptionConceptCode()
	{
		return this.optionConceptCode;
	}
	public void setOptionConceptCode(String optionConceptCode)
	{
		this.optionConceptCode = optionConceptCode;
	}
	public String getOptionDescription()
	{
		return this.optionDescription;
	}
	public void setOptionDescription(String optionDescription)
	{
		this.optionDescription = optionDescription;
	}
	public String getOptionName()
	{
		return this.optionName;
	}
	public void setOptionName(String optionName)
	{
		this.optionName = optionName;
	}
	
}
