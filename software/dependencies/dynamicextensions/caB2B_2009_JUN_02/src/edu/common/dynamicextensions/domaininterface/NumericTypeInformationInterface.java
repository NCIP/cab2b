/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domaininterface;

/**
 * This is a primitive attribute of type integar.Using this information a column of type integer is prepared.
 * @author geetika_bangard
 */
public interface NumericTypeInformationInterface extends AttributeTypeInformationInterface 
{

	
	/**
	 * This method returns the measurement units of this Attribute.
	 * The measurement units are shown in the dynamically created user interface.
	 * The measurement units are meter,kg,cm etc. They are displayed after the user input control. 
	 * @return the measurement units of this Attribute.
	 */
	String getMeasurementUnits();
	/**
	 * This method sets the measurement units of this Attribute.
	 * @param measurementUnits the measurement units to be set.
	 */
	void setMeasurementUnits(String measurementUnits);

	/**
	 * This method returns the places after the decimal point.
	 * @return the places after the decimal point.
	 */
	Integer getDecimalPlaces();

	/**
	 * This method sets the places after the decimal point of the DoubleAttribue.
	 * @param decimalPlaces the places after the decimal point to be set.
	 */
	 void setDecimalPlaces(Integer decimalPlaces);

	/**
	 * This method returns the length of the number in digits.
	 * @return the length of the number in digits. 
	 */
	 Integer getDigits();

	/**
	 * This method sets the length of the number in digits.
	 * @param digits the length of the number in digits.
	 */
	 void setDigits(Integer digits);
}
