/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.interfaces;

/**
 * @author deepti_shelar
 */
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;

public interface ControlUIBeanInterface
{
	/**
	 * 
	 * @return String name
	 */
	String getName();

	/**
	 * @param name The name to set.
	 */
	void setName(String name);

	/**
	 * If user has chosen it to be kept hidden.
	 * @return Returns the isHidden.
	 */
	Boolean getIsHidden();

	/**
	 * @param isHidden The isHidden to set.
	 */
	void setIsHidden(Boolean isHidden);

	/**
	 * The sequence Number for setting it at the desired place in the tree and so in the UI.
	 * @return Returns the sequenceNumber.
	 */
	Integer getSequenceNumber();

	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	void setSequenceNumber(Integer sequenceNumber);

	/**
	 * @return Returns the attribute.
	 *
	 */
	AbstractAttributeInterface getAbstractAttribute();

	/**
	 * @param attributeInterface The AbstractAttribute to set.
	 */
	void setAbstractAttribute(AbstractAttributeInterface attributeInterface);

	/**
	 * @return Returns the dataType.
	 */

	String getDataType();

	/**
	 * @param dataType The dataType to set.
	 */
	void setDataType(String dataType);

	/**
	 * @return Returns the dataTypeList.
	 */
	List getDataTypeList();

	/**
	 * @param dataTypeList The dataTypeList to set.
	 */
	void setDataTypeList(List dataTypeList);

	/**
	 * @return Returns the choiceList.
	 */
	/*
	 String getChoiceList() ;
	 *//**
	 * @param choiceList The choiceList to set.
	 */
	/*
	 void setChoiceList(String choiceList) ;*/

	/**
	 * @return the attributeCssClass
	 */
	String getCssClass();

	/**
	 * @param attributeCssClass the attributeCssClass to set
	 */
	void setCssClass(String attributeCssClass);

	/**
	 * @return the attributeTooltip
	 */
	String getTooltip();

	/**
	 * @param attributeTooltip the attributeTooltip to set
	 */
	void setTooltip(String attributeTooltip);

	/**
	 * @return the attributeCaption
	 */
	String getCaption();

	/**
	 * @param attributeTooltip the attributeTooltip to set
	 */
	void setCaption(String attributeTooltip);

	/**
	 * 
	 * @return If the control is a password field
	 */
	Boolean getIsPassword();

	/**
	 * 
	 * @param isPassword If it is a password field set as true
	 */
	void setIsPassword(Boolean isPassword);
	
	/**
	 * 
	 * @return If the control is a password field
	 */
	Boolean getIsUrl();

	/**
	 * 
	 * @param isPassword If it is a password field set as true
	 */
	void setIsUrl(Boolean isUrl);

	/**
	 * @return the toolsList
	 */
	/*
	 List getToolsList(); 

	 *//**
	 * @param toolsList the toolsList to set
	 */
	/*
	 void setToolsList(List toolsList); 
	 *//**
	 * @return the userSelectedTool
	 */
	/*
	 String getUserSelectedTool();

	 *//**
	 * @param userSelectedTool the userSelectedTool to set
	 */
	/*
	 void setUserSelectedTool(String userSelectedTool) ;*/
	/**
	 * @return Returns the columns.
	 */
	Integer getColumns();

	/**
	 * @param columns The columns to set.
	 */
	void setColumns(Integer columns);

	/**
	 * @return Returns the rows.
	 */
	Integer getRows();

	/**
	 * @param rows The rows to set.
	 */
	void setRows(Integer rows);

	/**
	 * @return Returns the isMultiSelect.
	 */
	Boolean getIsMultiSelect();

	/**
	 * @param isMultiSelect The isMultiSelect to set.
	 */
	void setIsMultiSelect(Boolean isMultiSelect);

	/**
	 * @return Returns the displayChoiceList.
	 */
	/*
	 List getDisplayChoiceList(); 
	 *//**
	 * @param displayChoiceList The displayChoiceList to set.
	 */
	/*
	 void setDisplayChoiceList(List displayChoiceList);*/
	/**
	 * 
	 * @param userSelectedTool userSelected Control
	 */
	void setUserSelectedTool(String userSelectedTool);

	/**
	 * 
	 * @param htmlFile htmlFile to be inserted
	 */
	void setHtmlFile(String htmlFile);

	/**
	 * @return the linesType
	 */
	String getLinesType();

	/**
	 * @param linesType the linesType to set
	 */
	void setLinesType(String linesType);

	/**
	 * 
	 * @return date value type as None/Today's date/selected date
	 */
	String getDateValueType();

	/**
	 * 
	 * @param dateValueType None/Today's date/selected date
	 */
	void setDateValueType(String dateValueType);

	/**
	 * Returns validationRules : value of checkbox fields selected by user
	 * @return String[] ValidationRules
	 */
	String[] getValidationRules();
	
	/**
	 * @return the attributeNoOfRows
	 */
	String getAttributeNoOfRows();

	/**
	 * @param attributeNoOfRows the attributeNoOfRows to set
	 */
	void setAttributeNoOfRows(String attributeNoOfRows);
	

	/**
	 * 
	 * @param validationRules value of checkbox fields
	 */
	void setValidationRules(String[] validationRules);
	
	//Selected form Attributes ids
	public String[] getSelectedAttributeIds();
	public void setSelectedAttributeIds(String[] selectedFormAttributeList);
	
	public List getSelectedAttributes();
	public void setSelectedAttributes(List selectedAttributes);
	//Separator
	public String getSeparator();
	public void setSeparator(String separator);
	
	
	//Added by Ashish
	public String getControlOperation();
	public String getSelectedControlId();
	public String getUserSelectedTool();
	public String getControlsSequenceNumbers();
}