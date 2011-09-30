
package edu.common.dynamicextensions.validation;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * @author chetan_patil
 *
 */
public class RequiredValidator implements ValidatorRuleInterface
{

	/** 
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 * @throws DynamicExtensionsValidationException
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map parameterMap) throws DynamicExtensionsValidationException
	{
		String attributeName = attribute.getName();
		if (valueObject != null)
		{
			if (valueObject instanceof List)
			{
				List valueList = (List) valueObject;
				if (valueList.isEmpty())
				{
					throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.RequiredValidator", attributeName);
				}
			}
			else if (valueObject instanceof String)
			{
				if (((String) valueObject).trim().equals(""))
				{
					throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.RequiredValidator", attributeName);
				}
			}
		}
		else
		{
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.RequiredValidator", attributeName);
		}

		return true;
	}
}
