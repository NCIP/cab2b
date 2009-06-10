package edu.wustl.cab2b.common.errorcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import edu.wustl.common.util.global.ApplicationProperties;

/**
 * Class to handle the error codes and the corresponding messages to be shown to the user.
 * @author gautam_shetty
 */
public class ErrorCodeHandler
{
	private static ResourceBundle bundle;
	
	public static void initBundle(String baseName)
	{
		bundle = ResourceBundle.getBundle(baseName);
	}
	
	public static String getValue(String theKey)
	{
		return bundle.getString(theKey);
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
		String msg = bundle.getString(theKey);
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
	
	/**
     * Returns the error message corresponding to the error code passed 
     * from the error codes file.
     * @param errorCode The error code
     * @return the error message corresponding to the error code passed 
     * from the error codes file.
     */
    public static String getErrorMessage(String errorCode)
    {
        return getValue(errorCode);
    }
}
