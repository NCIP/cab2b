
package edu.wustl.common.util.dbManager;

import java.io.PrintStream;
import java.io.PrintWriter;

import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DAOException extends Exception
{

	private Exception wrapException;

	/**
	 *  message initialised with super.getMessage()
	 */
	private String message = super.getMessage();

	/**
	 *  message which is used as supporting message to the main message
	 */
	private String supportingMessage;

	public DAOException(String message)
	{
		this(message, null);
	}

	public DAOException(Exception ex)
	{
		this(ex.getMessage(),ex);
	}

	/**
	 * Constructs DAOException object with SMEException object.
	 * @param exception SMException object.
	 */
	public DAOException(final SMException exception)
	{
		this("Security Exception: " + exception.getMessage(), exception);

//		Logger.out.error("Exception in Authorization: " + e.getMessage(), e);
	}

	/**
	 * @param wrapException The wrapException to set.
	 */
	public DAOException(String message, Exception wrapException)
	{
		super(message, wrapException);
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
	private void setWrapException(Exception wrapException)
	{
		this.wrapException = wrapException;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace()
	{
		super.printStackTrace();
		if (wrapException != null)
			wrapException.printStackTrace();
	}

	public void printStackTrace(PrintWriter thePrintWriter)
	{
		super.printStackTrace(thePrintWriter);
		if (wrapException != null)
			wrapException.printStackTrace(thePrintWriter);
	}

	public void printStackTrace(PrintStream thePrintStream)
	{
		super.printStackTrace(thePrintStream);
		if (wrapException != null)
			wrapException.printStackTrace(thePrintStream);
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return Returns the supportingMessage.
	 */
	public String getSupportingMessage()
	{
		return supportingMessage;
	}

	/**
	 * @param supportingMessage The supportingMessage to set.
	 */
	public void setSupportingMessage(String supportingMessage)
	{
		this.supportingMessage = supportingMessage;
	}
}