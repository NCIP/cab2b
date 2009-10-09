/*
 * Created on Aug 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;



/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BizLogicException extends Exception
{
	private Exception wrapException; 
	
	public BizLogicException()
	{
		
	}
	public BizLogicException(String message)
	{
		this(message,null);
	}
	
	public BizLogicException(Exception ex)
	{
		this("",ex);
	}
	
	/**
	 * @param wrapException The wrapException to set.
	 */
	public BizLogicException(String message, Exception wrapException)
	{
		/**Added by amit_doshi to fix the bug related to exception chaining
		 * code reviewer :- abhijit_naik
		 */
		super(message,wrapException);
		this.wrapException = wrapException;
	}
	
	/**
	 * @return Returns the wrapException.
	 */
	private Exception getWrapException()
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
	
}
