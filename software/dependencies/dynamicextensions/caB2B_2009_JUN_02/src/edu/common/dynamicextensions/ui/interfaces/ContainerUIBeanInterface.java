/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.interfaces;

import java.util.List;

public interface ContainerUIBeanInterface
{
	/**
	 * css style for the main table.
	 * @return Returns the mainTableCss.
	 */
	String getMainTableCss();

	/**
	 * @param mainTableCss The mainTableCss to set.
	 */
	void setMainTableCss(String mainTableCss);

	/**
	 * @return Returns the requiredFieldIndicatior.
	 */
	String getRequiredFieldIndicatior();

	/**
	 * @param requiredFieldIndicatior The requiredFieldIndicatior to set.
	 */
	void setRequiredFieldIndicatior(String requiredFieldIndicatior);

	/**
	 * @return Returns the requiredFieldWarningMessage.
	 */
	String getRequiredFieldWarningMessage();

	/**
	 * @param requiredFieldWarningMessage The requiredFieldWarningMessage to set.
	 */
	void setRequiredFieldWarningMessage(String requiredFieldWarningMessage);

	/**
	 * css style for the Title.
	 * @return Returns the titleCss.
	 */
	String getTitleCss();

	/**
	 * @param titleCss The titleCss to set.
	 */
	void setTitleCss(String titleCss);

	/**
	 * The css style defined for button.
	 * @return Returns the buttonCss.
	 */
	String getButtonCss();

	/**
	 * @param buttonCss The buttonCss to set.
	 */
	void setButtonCss(String buttonCss);

	/**
	 * caption for the container.
	 * @return Returns the caption.
	 */
	String getFormCaption();

	/**
	 * @param caption The caption to set.
	 */
	void setFormCaption(String caption);

	/**
	 * 
	 * @return String  FormName
	 */
	String getFormName();

	/**
	 * 
	 * @param formName FormName
	 */
	void setFormName(String formName);
	/**
	 * 
	 * @return
	 */
	List getFormList();
	/**
	 * 
	 * @param formList
	 */
	void setFormList(List formList);
	
	/**
	 * 
	 * @return
	 */
	 String getParentForm();
	 
	 /**
	  * 
	  * @param parentForm
	  */
	 void setParentForm(String parentForm);
}
