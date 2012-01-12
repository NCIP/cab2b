
package edu.wustl.common.querysuite.exceptions;
/**
 * Exception will be thrown if there already exists a child with the
 * given output Entity and association.
 * @author prafull_kadam
 *
 */
public class DuplicateChildException extends Exception
{

	private static final long serialVersionUID = 1325474504982453845L;

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public DuplicateChildException()
	{
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * @param message The detail message
	 */
	public DuplicateChildException(String message)
	{
		super(message);
	}
	
}
