/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.exceptionformatter;

import java.text.MessageFormat;

import edu.wustl.common.util.global.ApplicationProperties;

public class DefaultExceptionFormatter implements ExceptionFormatter {

	public String formatMessage(Exception objExcp, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getErrorMessage(String key,Object[] args)
	{
		String message=null;
		message=ApplicationProperties.getValue(key);
		if(message!=null && args!=null)
		{
			message=MessageFormat.format(message,args);
		}
		return message;
	}
}
