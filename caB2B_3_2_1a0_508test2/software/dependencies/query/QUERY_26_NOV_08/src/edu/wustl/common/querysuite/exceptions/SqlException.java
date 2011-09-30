/**
 * 
 */

package edu.wustl.common.querysuite.exceptions;

/**
 * @author prafull_kadam
 * It will be thrown by SQL generator, when there is error in the passed IQuery object.
 */
public class SqlException extends Exception
{

	private static final long serialVersionUID = -1971145240336059949L;

	/**
	 * @param message The error Message.
	 */
	public SqlException(String message)
	{
		super(message);
	}

	/**
	 * @param message  The error Message.
	 * @param cause The cause of Exception.
	 */
	public SqlException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	

}
