/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.common.dynamicextensions.validation;

import java.util.ArrayList;
import java.util.Map;

import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * TextLengthValidator Class validates the text as per the text length defined for that Control during its creation.
 * If the no text length is provided or the text lenght is zero, no validation checks are made.   
 * @author chetan_patil
 */
public class TextLengthValidator implements ValidatorRuleInterface
{

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject,
			Map<String, String> parameterMap) throws DynamicExtensionsValidationException
	{
		boolean isValid = false;
		AttributeTypeInformationInterface attributeTypeInformation = attribute
				.getAttributeTypeInformation();
		String attributeName = attribute.getName();
		if (valueObject != null)
		{
			String value = (String) valueObject;
			if (attributeTypeInformation != null
					&& attributeTypeInformation instanceof StringAttributeTypeInformation)
			{
				ArrayList<String> placeHolders = new ArrayList<String>();

				StringAttributeTypeInformation stringAttributeTypeInformation = (StringAttributeTypeInformation) attributeTypeInformation;
				Integer size = stringAttributeTypeInformation.getSize();
				if ((size > 0) && (value.length() > size))
				{
					placeHolders.add(attributeName);
					placeHolders.add((new Long(size)).toString());
					throw new DynamicExtensionsValidationException("Validation failed", null,
							"dynExtn.validation.TextLength", placeHolders);
				}
				else
				{
					isValid = true;
				}
			}
		}

		return isValid;
	}
}
