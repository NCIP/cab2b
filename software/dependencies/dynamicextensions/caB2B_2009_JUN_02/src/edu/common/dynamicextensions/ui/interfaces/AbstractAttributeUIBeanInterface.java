/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.ui.interfaces;

import java.util.List;

/**
 * 
 * @author deepti_shelar
 *
 */
public interface AbstractAttributeUIBeanInterface
{

	/**
	 * Returns the attributesize
	 * @return String AttributeSize
	 */
	String getAttributeSize();

	/**
	 * @param attributeSize The attributeSize to set.
	 */
	void setAttributeSize(String attributeSize);

	/**
	 * @return Returns the attributeFormat.
	 */
	 String getFormat();

	/**
	 * @param attributeFormat The attributeFormat to set.
	 */
	 void setFormat(String attributeFormat);

	/**
	 * @return Returns the attributeDefaultValue.
	 */
	 String getAttributeDefaultValue();

	/**
	 * @param attributeDefaultValue The attributeDefaultValue to set.
	 */
	 void setAttributeDefaultValue(String attributeDefaultValue);

	/**
	 * @return Returns the attributeDisplayUnits.
	 */
	 String getAttributeDisplayUnits();

	/**
	 * @param attributeDisplayUnits The attributeDisplayUnits to set.
	 */
	 void setAttributeDisplayUnits(String attributeDisplayUnits);

	/**
	 * @return Returns the attributeDecimalPlaces.
	 */
	 String getAttributeDecimalPlaces();

	/**
	 * @param attributeDecimalPlaces The attributeDecimalPlaces to set.
	 */
	 void setAttributeDecimalPlaces(String attributeDecimalPlaces);

	/**
	 * @return Returns the attributeIdentifier.
	 */
	 String getAttributeIdentifier();

	/**
	 * @param attributeIdentifier The attributeIdentifier to set.
	 */
	 void setAttributeIdentifier(String attributeIdentifier);

	/**
	 * @return the attributeMeasurementUnits
	 */
	 String getAttributeMeasurementUnits();

	/**
	 * @param attributeMeasurementUnits the attributeMeasurementUnits to set
	 */
	 void setAttributeMeasurementUnits(String attributeMeasurementUnits);

	/**
	 * @return Returns the dataType.
	 */

	 String getDataType();

	/**
	 * @param dataType The dataType to set.
	 */
	 void setDataType(String dataType);

	/**
	 * Number of digits before decimal 
	 * @return String AttributeDigits
	 */
	 String getAttributeDigits();

	/**
	 * @param attributeDigits : Number of digits before decimal
	 */
	 void setAttributeDigits(String attributeDigits);

	/**
	 * 
	 * @return Name
	 */
	 String getName();

	/**
	 * 
	 * @param name Name
	 */
	 void setName(String name);

	/**
	 * 
	 * @return Description
	 */
	 String getDescription();

	/**
	 * 
	 * @param name Description
	 */
	 void setDescription(String name);

	
	/**
	 * @return Returns the displayChoice.
	 */
	 String getDisplayChoice();

	/**
	 * @param displayChoice The displayChoice to set.
	 */
	 void setDisplayChoice(String displayChoice);

	/**
	 * 
	 * @return Concept code
	 */
	 String getAttributeConceptCode();

	/**
	 * 
	 * @param conceptCode Concept code
	 */
	 void setAttributeConceptCode(String conceptCode);

	/**
	 * 
	 * @return String DateValueType
	 */
	 String getDateValueType();

	/**
	 * 
	 * @param dateValueType dateValueType
	 */
	 void setDateValueType(String dateValueType);

	/**
	 * 
	 * @return String AttributeIdentified
	 */
	 String getAttributeIdentified();

	/**
	 * 
	 * @param attributeIdentified attributeIdentified
	 */
	 void setAttributeIdentified(String attributeIdentified);

	/**
	 * Returns validationRules : value of checkbox fields selected by user
	 * @return String[] ValidationRules
	 */
	String[] getValidationRules();

	/**
	 * 
	 * @param validationRules value of checkbox fields
	 */
	void setValidationRules(String[] validationRules);

	/**
	 * @return the max
	 */
	String getMax();

	/**
	 * @param max the max to set
	 */
	void setMax(String max);
	
	/**
	 * @param max the max to set
	 */
	void setMaxTemp(String maxTemp);


	/**
	 * @return the min
	 */
	String getMin();

	/**
	 * @param min the min to set
	 */
	void setMin(String min);
	
	/**
	 * @param min the min to set
	 */
	void setMinTemp(String minTemp);
	
	/**
	 * @return the tempValidationRules
	 */
	public String[] getTempValidationRules();

	/**
	 * @param tempValidationRules the tempValidationRules to set
	 */
	public void setTempValidationRules(String[] tempValidationRules);
	
	/**
	 * get the list of concept codes for a particular attribute 
	 * @return list of concept codes for each option of attribute
	 */
	public String[] getOptionConceptCodes();
	
	/**
	 * set the list of concept codes for a particular attribute 
	 * @param list of concept codes for each option of attribute
	 */
	public void setOptionConceptCodes(String[] optionConceptCodes);

	/**
	 * set the list of descriptions for each option of an attribute 
	 * @param list of description for each option of attribute
	 */
	public String[] getOptionDescriptions();

	/**
	 * 
	 * @param optionDescriptions : list of description for each option of attribute
	 */
	public void setOptionDescriptions(String[] optionDescriptions);
	
	/**
	 * 
	 * @return list of names for each option of attribute
	 */
	public String[] getOptionNames();

	/**
	 * 
	 * @param optionNames list of names for each option of attribute
	 */
	public void setOptionNames(String[] optionNames);
	
	/**
	 * 
	 * @return OptionDetailObject list containing option name, description and concept code for each option
	 */
	public List getOptionDetails();
	
	/**
	 * 
	 * @param OptionDetailObject list containing option name, description and concept code for each option
	 */
	public void setOptionDetails(List optionDetails);
	
	/**
	 * 
	 * @return List of file formats supported for a file control/attribute.
	 */
	public String[] getFileFormats();

	/**
	 * 
	 * @param fileFormats List of file formats supported for a file control/attribute.
	 */
	public void setFileFormats(String[] fileFormats);
	public String getGroupName();
	public void setGroupName(String groupName);
	public String getFormName();
	public void setFormName(String formName);
	public String getCsvString();
	public void setCsvString(String csvString);
	
	//Added by Ashish
	public String getMeasurementUnitOther();
}
