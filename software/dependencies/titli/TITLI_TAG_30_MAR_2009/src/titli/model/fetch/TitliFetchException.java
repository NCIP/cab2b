/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package titli.model.fetch;

import titli.model.TitliException;

/**
 * @author Juber Patel
 *
 */
public class TitliFetchException extends TitliException
{
	/**
	 * the default constructor
	 *
	 */
	public TitliFetchException()
	{
		super();
				
	}
	
	
	/**
	 * 
	 * @param message the message
	 */
	public TitliFetchException(String message)
	{
		super(message);
	}
	
	
	/**
	 * 
	 * @param errorCode the error code
	 * @param message the message 
	 * @param cause the cause
	 */
	public TitliFetchException(String errorCode, String message, Throwable cause)
	{
		super(errorCode, message, cause);
		
	}

}
