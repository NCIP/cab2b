/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package titli.model.index;

import titli.model.TitliException;

/**
 * @author Juber Patel
 *
 */
public class TitliIndexException extends TitliException 
{

	/**
	 * default constructor
	 */
	public TitliIndexException() 
	{
		super();
	}

	/**
	 * @param message the message
	 */
	public TitliIndexException(String message) 
	{
		super(message);
		
	}

	/**
	 * @param errorCode the error code
	 * @param message the message
	 * @param cause the cause
	 */
	public TitliIndexException(String errorCode, String message, Throwable cause) 
	{
		super(errorCode, message, cause);
		
	}

}
