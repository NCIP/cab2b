/**
 * 
 */

package edu.wustl.common.security.exceptions;

/**
 * @author prafull_kadam
 * Class to wrap Password Encryption/Decryption related exception.
 */
public class PasswordEncryptionException extends Exception
{

	/**
	 * Default constructor
	 */
	public PasswordEncryptionException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message the detail message. The detail message is saved for later retrieval by the getMessage() method.
	 */
	public PasswordEncryptionException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause the cause (which is saved for later retrieval by the getCause() method).
	 */
	public PasswordEncryptionException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message the detail message.
	 * @param cause the cause (which is saved for later retrieval by the getCause() method).
	 */
	public PasswordEncryptionException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
