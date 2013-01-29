/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.queryobject;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * A condition containing an attribute, relational operator and value(s).
 * e.g. participant.sex = 'Male'
 * 
 * @version 1.0
 * @updated 11-Oct-2006 02:56:07 PM
 */
public interface ICondition extends IBaseQueryObject, IParameterizable
{

	/**
	 * To get the left operand of the condition.
	 * @return The Dynamic extension attribute, the left operand of the condition.
	 */
	AttributeInterface getAttribute();

	/**
	 * To get the relational operator of condition.
	 * @return The relational operator of the Condition.
	 */
	RelationalOperator getRelationalOperator();

	/**
	 * To get the List of String representing value part of the condition.
	 * @return List of String representing value part of the condition.
	 */
	List<String> getValues();

	/**
	 * basically calls getValues(0)
	 * @return The String representing first value in the value list of the condition.
	 */
	String getValue();

	/**
	 * To set the attribute in the condition.
	 * @param attribute The reference to the Synamic Extension attribute on which condition to be formed.
	 * 
	 */
	void setAttribute(AttributeInterface attribute);

	/**
	 * To set relational operator for the Condition.
	 * @param relationalOperator reference to RelationalOperator.
	 */
	void setRelationalOperator(RelationalOperator relationalOperator);

	/**
	 * Basically calls setValue(0, value). Use this for unary operators.
	 * @param value The String representing value part of the condition. 
	 */
	void setValue(String value);

	/**
	 *  To set the list of values for the Condtion.
	 * @param values The List of values to set
	 * 
	 */
	void setValues(List<String> values);

	/**
	 * To add value in the value list of condition.
	 * @param value The String representing one of the value in value list of condition.
	 */
	void addValue(String value);

}
