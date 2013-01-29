/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package titli.model.search;

import titli.model.TitliException;

/**
 * @author Juber Patel
 *
 */
public class TitliSearchException extends TitliException 
{

	/**
	 *default constructor 
	 */
	public TitliSearchException() 
	{
		super();
	}

	/**
	 * @param message the message
	 */
	public TitliSearchException(String message) 
	{
		super(message);
		
	}

	/**
	 * @param errorCode the error code
	 * @param message the mesage
	 * @param cause the cause
	 */
	public TitliSearchException(String errorCode, String message, Throwable cause) 
	{
		super(errorCode, message, cause);
		
	}

}
