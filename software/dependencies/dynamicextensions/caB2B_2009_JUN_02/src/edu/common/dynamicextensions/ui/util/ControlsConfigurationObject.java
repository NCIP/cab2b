/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.util;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author deepti_shelar
 *
 */
public class ControlsConfigurationObject
{
	private String controlName;
	private String displayLabel;
	private String jspName;
	private String imageFilePath;
	private List commonValidationRules ;
	private List commonImplicitRules ;
	private List commonExplicitRules ;
	private Map dataTypeValidationRules ;
	private List dataTypesList ;
	private Map dataTypeImplicitRules ;
	private Map dataTypeExplicitRules ;
	
	
	/**
	 * @return the controlName
	 */
	public String getControlName()
	{
		return controlName;
	}
	/**
	 * @param controlName the controlName to set
	 */
	public void setControlName(String controlName)
	{
		this.controlName = controlName;
	}
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
	 * @return the jspName
	 */
	public String getJspName()
	{
		return jspName;
	}
	/**
	 * @param jspName the jspName to set
	 */
	public void setJspName(String jspName)
	{
		this.jspName = jspName;
	}
	/**
	 * @return the dataTypes
	 */
	public List getDataTypesList()
	{
		return dataTypesList;
	}
	/**
	 * @param dataTypesList the dataTypes to set
	 */
	public void setDataTypesList(List dataTypesList)
	{
		this.dataTypesList = dataTypesList;
	}
	/**
	 * @return the commonValidationRules
	 */
	public List getCommonValidationRules()
	{
		return commonValidationRules;
	}
	/**
	 * @param commonValidationRules the commonValidationRules to set
	 */
	public void setCommonValidationRules(List commonValidationRules)
	{
		this.commonValidationRules = commonValidationRules;
	}
	/**
	 * @return the dataTypeValidationRules
	 */
	public Map getDataTypeValidationRules()
	{
		return dataTypeValidationRules;
	}
	/**
	 * @param dataTypeValidationRules the dataTypeValidationRules to set
	 */
	public void setDataTypeValidationRules(Map dataTypeValidationRules)
	{
		this.dataTypeValidationRules = dataTypeValidationRules;
	}
	/**
	 * @return the dataTypeExplicitRules
	 */
	public Map getDataTypeExplicitRules()
	{
		return dataTypeExplicitRules;
	}
	/**
	 * @param dataTypeExplicitRules the dataTypeExplicitRules to set
	 */
	public void setDataTypeExplicitRules(Map dataTypeExplicitRules)
	{
		this.dataTypeExplicitRules = dataTypeExplicitRules;
	}
	/**
	 * @return the dataTypeImplicitRules
	 */
	public Map getDataTypeImplicitRules()
	{
		return dataTypeImplicitRules;
	}
	/**
	 * @param dataTypeImplicitRules the dataTypeImplicitRules to set
	 */
	public void setDataTypeImplicitRules(Map dataTypeImplicitRules)
	{
		this.dataTypeImplicitRules = dataTypeImplicitRules;
	}
	/**
	 * @return the commonExplicitRules
	 */
	public List getCommonExplicitRules()
	{
		return commonExplicitRules;
	}
	/**
	 * @param commonExplicitRules the commonExplicitRules to set
	 */
	public void setCommonExplicitRules(List commonExplicitRules)
	{
		this.commonExplicitRules = commonExplicitRules;
	}
	/**
	 * @return the commonImplicitRules
	 */
	public List getCommonImplicitRules()
	{
		return commonImplicitRules;
	}
	/**
	 * @param commonImplicitRules the commonImplicitRules to set
	 */
	public void setCommonImplicitRules(List commonImplicitRules)
	{
		this.commonImplicitRules = commonImplicitRules;
	}
	public String getImageFilePath()
	{
		return this.imageFilePath;
	}
	public void setImageFilePath(String imageFilePath)
	{
		this.imageFilePath = imageFilePath;
	}
}
