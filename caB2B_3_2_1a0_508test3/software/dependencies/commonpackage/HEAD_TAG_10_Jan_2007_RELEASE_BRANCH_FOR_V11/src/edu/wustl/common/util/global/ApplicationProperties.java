/*
 * Created on Jul 19, 2004
 *
 */

package edu.wustl.common.util.global;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import sun.security.action.GetLongAction;
import sun.util.logging.resources.logging;

/**
 * This class is used to retrieve values of keys from the ApplicationResources.properties file.
 * @author kapil_kaveeshwar
 */
public class ApplicationProperties
{

	private static ResourceBundle bundle;
	private static Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ApplicationProperties.class);
	public static void initBundle(String baseName)
	{
		bundle = ResourceBundle.getBundle(baseName);
	
	}

	public static String getValue(String theKey)
	{
		String val="";
		if(bundle == null)
		{
			logger.fatal("resource bundle is null cannot return value for key " + theKey);
		}
		else
		{
			val= bundle.getString(theKey);
		}
		return val;
	}

/**
 * This method should be used when you want to customize error message with multiple replacement parameters
 * 
 * @param theKey - error key
 * @param placeHolders - replacement Strings
 * @return - complete error message
 */	
	public static String getValue(String theKey, List placeHolders)
	{
		String msg = "";
		if(bundle == null)
		{
			logger.fatal("resource bundle is null cannot return value for key " + theKey);
			return msg;
		}
		else
		{
			msg = bundle.getString(theKey);
		}

		StringBuffer message = new StringBuffer(msg);

		for (int i = 0; i < placeHolders.size(); i++)
		{
			message.replace(message.indexOf("{"), message.indexOf("}") + 1, (String) placeHolders.get(i));
		}
		return message.toString();
	}
	
	/**
	 * This method should be used when you want to customize error message with single replacement parameter
	 * 
	 * @param theKey - error key
	 * @param placeHolders - replacement Strings
	 * @return - complete error message
	 */	
		public static String getValue(String theKey, String placeHolder)
		{
			List temp = new ArrayList();
			temp.add(placeHolder);
			return getValue(theKey,temp);
		}


}