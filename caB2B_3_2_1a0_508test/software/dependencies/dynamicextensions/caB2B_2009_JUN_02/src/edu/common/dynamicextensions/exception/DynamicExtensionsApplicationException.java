/**
 *<p>Title: DynamicExtensionsApplicationException</p>
 *<p>Description: Application level exception for dynamic extension  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.exception;

import java.util.ArrayList;
import java.util.List;

/**
 *<p>Title: DynamicExtensionsApplicationException</p>
 *<p>Description:This exception class represents all the exceptions which are application specific 
 *for example 
 *<BR> Duplicate name for the entity is found. 
 *<BR> Backend Validation is failed <BR>
 * When these exceptions are caught , they are supposed to be processed so that user is shown
 * approprite error message which is extracted from the exception. In such exception cases user 
 * should not be shown error page.
 *</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */
public class DynamicExtensionsApplicationException extends BaseDynamicExtensionsException
{

	/**
	 * @param wrapException The wrapException to set.
	 */
	public DynamicExtensionsApplicationException(String message, Exception wrapException)
	{
		super(message, wrapException);
	}

	/**
	 * @param wrapException The wrapException to set.
	 */
	public DynamicExtensionsApplicationException(String message, Exception wrapException,
			String errorCode)
	{
		super(message, wrapException,errorCode);
	}

	/**
	 * 
	 * @param message
	 */
	public DynamicExtensionsApplicationException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param wrapException
	 * @param errorCode
	 */
	public DynamicExtensionsApplicationException(String message, Exception wrapException,
			String errorCode, List<String> placeHolders)
	{
		super(message, wrapException,errorCode);
		for(String placeHolder: placeHolders)
		{
			this.placeHolderList.add(placeHolder);
		}
	}

	/**
	 * @param message
	 * @param wrapException
	 * @param errorCode
	 */
	public DynamicExtensionsApplicationException(String message, Exception wrapException,
			String errorCode, String singlePlaceHolder)
	{
		this(message, wrapException, errorCode);
		this.placeHolderList.add(singlePlaceHolder);
	}
}
