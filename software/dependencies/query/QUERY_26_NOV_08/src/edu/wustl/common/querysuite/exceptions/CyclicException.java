/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.querysuite.exceptions;

/**
 * @author Mandar Shidhore
 * @version 1.0
 * @created 17-Oct-2006 16.27.04 PM
 * This Exception will be thrown when adding an Edge in graph causes cycle in the graph. 
 */

public class CyclicException extends Exception
{

	/**
	 * @param message The detailed message.
	 */
	public CyclicException(String message)
	{
		super(message);
	}
}
