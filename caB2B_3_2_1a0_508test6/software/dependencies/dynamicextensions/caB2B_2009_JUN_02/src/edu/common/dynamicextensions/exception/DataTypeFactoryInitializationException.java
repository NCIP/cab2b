
package edu.common.dynamicextensions.exception;


/**
 * @author chetan_patil
 */
public class DataTypeFactoryInitializationException extends DynamicExtensionsSystemException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private Exception wrapException;

	/**
	 * This is a parameterized Constructor method accepting an exception message.
	 * @param message Exception message
	 */
	public DataTypeFactoryInitializationException(String message)
	{
		this(message, null);
	}

	/**
	 * This is a parameterized Constructor method an exception object.
	 * @param exception Exception
	 */
	public DataTypeFactoryInitializationException(Exception exception)
	{
		this("", exception);
	}

	/**
	 * This is a parameterized Constructor method accepting an exception message and an Exception object.
	 * @param message Exception message
	 * @param wrapException Exception
	 */
	public DataTypeFactoryInitializationException(String message, Exception wrapException)
	{
		super(message);
		this.wrapException = wrapException;
	}

	/**
	 * @return Returns the wrapException.
	 */
	public Exception getWrapException()
	{
		return wrapException;
	}

	/**
	 * @param wrapException The wrapException to set.
	 */
	public void setWrapException(Exception wrapException)
	{
		this.wrapException = wrapException;		
	}
	
}
