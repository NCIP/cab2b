/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *<p>Title: DynamicExtensionsSystemException</p>
 *<p>Description: System level exception for dynamic extensions </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.exception;

/**
 *<p>Title:DynamicExtensionsSystemException </p>
 *<p>Description: This exception class represents a system level exception for example
 *<BR> Conncection to the database is reset or lost.
 *<BR> Hibernate exception is thrown or JDBCException is thrown <BR>
 *In such cases whenever this excepion is cuaght, application cannot recover from the situation
 *and error page is shown to the user specifying that fatal error has occured.
 *Proper logging should be done with the detailed stack trace using the wrapped exception.</p>
 *@author Vishvesh Mulay
 */
public class DynamicExtensionsSystemException
        extends
            BaseDynamicExtensionsException
{

    /**
     * @param wrapException The wrapException to set.
     * @param message message
     */
    public DynamicExtensionsSystemException(String message,
            Exception wrapException)
    {
    	super(message,wrapException);
    }

    /**
     * @param wrapException The wrapException to set.
     * @param errorCode error code
     * @param message message
     */
    public DynamicExtensionsSystemException(String message,
            Exception wrapException, String errorCode)
    {
       super(message,wrapException,errorCode);
    }

    /**
     * @param wrapException The wrapException to set.
     */
    /*
     private void DynamicExtensionsSystemException(Exception wrapException) {
     this(null,wrapException);
     }*/
    /**
     * 
     * @param message message
     */
    public DynamicExtensionsSystemException(String message)
    {
        this(message, null);
    }
}
