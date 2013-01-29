/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as a base exception from which all the exceptions will extend.
 * The class holds the information about the cause of the exception along with the unique error code.
 * @author Vishvesh Mulay
 *
 */


public abstract class BaseDynamicExtensionsException extends Exception
		implements
			DynamicExtensionExceptionInterface
{

	/**Detailed error message explaining the cause of the exception*/
	protected String errorCode;

	protected List<String> placeHolderList = new ArrayList<String>();

	 /**
     * @param wrapException The wrapException to set.
     * @param errorCode error code
     * @param message message
     */
    public BaseDynamicExtensionsException(String message,
            Exception wrapException, String errorCode)
    {
    	super(message,wrapException);
        this.errorCode = errorCode;
    }
	
	/**
     * @param wrapException The wrapException to set.
     * @param message message
     */
    public BaseDynamicExtensionsException(String message,
            Exception wrapException)
    {
    	super(message,wrapException);
    }
    
    /**
	 * @param message
	 * @param wrapException
	 * @param errorCode
	 */
	public BaseDynamicExtensionsException(String message, Exception wrapException,
			String errorCode, List<String> placeHolders)
	{
		this(message,wrapException,errorCode);
		this.placeHolderList = placeHolders;
	}
    /**
     * 
     * @param message message
     */
    public BaseDynamicExtensionsException(String message)
    {
       super(message);
    }

	/**
	 * Getter method for errorCode
	 * @return errorCode errorCode that is set
	 */
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * Setter method for errorCode
	 * @param errorCode errorCode to set.
	 */
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	/**
	 * @return Returns the placeHolderList.
	 */
	public List<String> getPlaceHolderList()
	{
		return placeHolderList;
	}

	
	/**
	 * @param placeHolderList The placeHolderList to set.
	 */
	public void setPlaceHolderList(List<String> placeHolderList)
	{
		this.placeHolderList = placeHolderList;
	}

}
