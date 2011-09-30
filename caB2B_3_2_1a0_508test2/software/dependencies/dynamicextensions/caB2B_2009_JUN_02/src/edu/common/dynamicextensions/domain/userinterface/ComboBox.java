
package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.wustl.common.beans.NameValueBean;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_COMBOBOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ComboBox extends SelectControl implements ComboBoxInterface
{

	/**
	 * Serial Version Unique Identifier. 
	 */
	private static final long serialVersionUID = 3062212342005513616L;

	/**
	 * List of Choices.
	 */
	List listOfValues = null;

	/**
	 * Empty Constructor
	 */
	public ComboBox()
	{
	}

	/**
	 * This method generates the HTML code for ComboxBox control on the HTML form
	 * @return HTML code for ComboBox
	 * @throws DynamicExtensionsSystemException if HTMLComponentName() fails.
	 */
	public String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		String htmlString = "<SELECT class='" + cssClass + "' name='" + getHTMLComponentName()
				+ "' " + "id='" + getHTMLComponentName() + "'>";

		String defaultValue = "";
		if (this.value != null)
		{
			if (this.value instanceof String)
			{
				defaultValue = (String) this.value;
			}
			else if (this.value instanceof List)
			{
				List valueList = (List) this.value;
				if (!valueList.isEmpty())
				{
					defaultValue = valueList.get(0).toString();
				}
			}
		}
		else
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
			if (defaultValue == null || defaultValue.length() == 0)
			{
				defaultValue = "";
			}
		}

		List<NameValueBean> nameValueBeanList = null;

		if (listOfValues == null)
		{
			nameValueBeanList = ControlsUtility.populateListOfValues(this);
		}

		if (nameValueBeanList != null && nameValueBeanList.size() > 0)
		{
			for (NameValueBean nameValueBean : nameValueBeanList)
			{
				if (nameValueBean.getValue().equals(defaultValue))
				{
					htmlString += "<OPTION VALUE='" + nameValueBean.getValue() + "' SELECTED>"
							+ nameValueBean.getName();
				}
				else
				{
					htmlString += "<OPTION VALUE='" + nameValueBean.getValue() + "'>"
							+ nameValueBean.getName();
				}
			}
		}
		htmlString += "</SELECT>";

		return htmlString;
	}

	/**
	 * This method returns the list of values that are displayed as choices.
	 * @return the list of values that are displayed as choices.
	 */
	public List getChoiceList()
	{
		return listOfValues;
	}

	/**
	 * This method sets the list of values that are displayed as choices.
	 * @param choiceList the List of values that is to set as ChoiceList.
	 */
	public void setChoiceList(List choiceList)
	{
		listOfValues = choiceList;
	}

	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		String htmlString = "&nbsp;";
		
		String defaultValue = "";
		if (this.value != null)
		{
			if (this.value instanceof String)
			{
				defaultValue = (String) this.value;
			}
			else if (this.value instanceof List)
			{
				List valueList = (List) this.value;
				if (!valueList.isEmpty())
				{
					defaultValue = valueList.get(0).toString();
				}
			}
		}
		
		List<NameValueBean> nameValueBeanList = null;
		if (listOfValues == null)
		{
			nameValueBeanList = ControlsUtility.populateListOfValues(this);
		}
		
		if (nameValueBeanList != null && nameValueBeanList.size() > 0)
		{
			for (NameValueBean nameValueBean : nameValueBeanList)
			{
				if (nameValueBean.getValue().equals(defaultValue))
				{
					htmlString = "<span class='" + cssClass + "'> " + nameValueBean.getName() + "</span>";
					break;
				}
			}
		}
		
		return htmlString;
	}

}