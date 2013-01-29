/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Title: DynamicExtensionsApplicationException</p>
 *<p>Description: Application level exception for dynamic extension  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.exception;

import java.util.List;

/**
 *<p>Title: DynamicExtensionsValidationException</p>
 *<p>Description:This Exception is thrown by a  ValidatorRuleInterface when validation fails
 *</p>
 *@author Rahul Ner
 *@version 1.0
 */
public class DynamicExtensionsValidationException extends DynamicExtensionsApplicationException
{

	/**
	 * @param message msg
	 * @param wrapException The wrapException to set.
	 */
	public DynamicExtensionsValidationException(String message, Exception wrapException)
	{
		super(message, wrapException);
	}

	/**
	 * @param message message
	 * @param wrapException wrapException
	 * @param errorCode errorCode
	 */
	public DynamicExtensionsValidationException(String message, Exception wrapException, String errorCode)
	{
		super(message, wrapException, errorCode);
	}

	/**
	 * @param message message
	 */
	public DynamicExtensionsValidationException(String message)
	{
		this(message, null);
	}

	/**
	 * @param message message
	 * @param wrapException wrapException
	 * @param errorCode errorCode
	 * @param placeHolders placeHolders
	 */
	public DynamicExtensionsValidationException(String message, Exception wrapException, String errorCode, List<String> placeHolders)
	{
		super(message, wrapException, errorCode, placeHolders);
	}

	/**
	 * @param message message
	 * @param wrapException wrapException
	 * @param errorCode errorCode
	 * @param singlePlaceHolder singlePlaceHolder
	 */
	public DynamicExtensionsValidationException(String message, Exception wrapException, String errorCode, String singlePlaceHolder)
	{
		super(message, wrapException, errorCode, singlePlaceHolder);
	}

}
