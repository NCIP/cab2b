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
 * This exception will be thrown by getRoot method when there are multpile roots present in a graph.
 */

public class MultipleRootsException extends Exception
{

	/**
	 * @param message The detailed message.
	 */
	public MultipleRootsException(String message)
	{
		super(message);
	}
}
