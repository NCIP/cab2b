/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.validation;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * Any validator that is to be invoked for a attribute of an dynamic extension entity
 * should implement this interface.
 * @author sujay_narkar
 * @author Rahul Ner
 */
public interface ValidatorRuleInterface
{

	/**
	 * This method validates input attribute for a particular validation
	 * for input valueObject.
	 * 
	 * @param attribute  attribute to be validated
	 * @param valueObject  value of the object 
	 * @param paramMap RuleConfigurations.xml defines parameter for particular rule.
	 *                               key = parameter name
	 *                               value = parameter value
	 * @return  boolean if no error it return true
	 * @throws DynamicExtensionsValidationException : Validation Exception
	 */
	boolean validate(AttributeInterface attribute, Object valueObject, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException,DynamicExtensionsSystemException;
}
