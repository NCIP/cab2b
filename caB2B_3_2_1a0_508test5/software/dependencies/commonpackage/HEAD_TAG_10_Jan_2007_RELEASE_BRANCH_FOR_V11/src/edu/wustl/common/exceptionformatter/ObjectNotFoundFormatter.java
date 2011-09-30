package edu.wustl.common.exceptionformatter;

import java.sql.Connection;
import java.text.MessageFormat;

import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class ObjectNotFoundFormatter implements ExceptionFormatter {

	public String formatMessage(Exception objExcp, Object[] args) {
		// TODO Auto-generated method stub
		String formattedErrMsg=null;
		String temp1= "exists: ";
		String temp2= "class: ";
		String temp3= "given ";
		int startIndex = -1;
		int endIndex = -1;
		int tempIndex = -1;
		Connection connection=null;
		if(args[1]!=null)
        {
            connection =(Connection)args[1];
        }
        else
        {
            Logger.out.debug("Error Message: Connection object not given");
        }
		try
		{
			// get Message from exception object
			String message=objExcp.getMessage();
			
			// get column name from message
			startIndex = message.indexOf(temp1)-1;
			tempIndex=message.indexOf(temp3)+temp3.length();
			String columnName=message.substring(tempIndex,startIndex);
			Logger.out.debug(columnName+"--"+columnName.length());
			
			// get column value on from message for which object was not found   
			startIndex = message.indexOf(temp1)+temp1.length();
			endIndex = message.indexOf(",",startIndex);
			String value = message.substring(startIndex,endIndex);
			Logger.out.debug(value+"  "+value.length());
			
			// get class name from message
			startIndex = message.indexOf(temp2)+temp2.length();
			String className = message.substring(startIndex);
			Logger.out.debug(className+"--"+className.length());
			Class classObj = Class.forName(className);
			// get table name from class
			String displayName = ExceptionFormatterFactory.getDisplayName(HibernateMetaData.getTableName(classObj),connection);
			
			Object[] arguments = new Object[]{displayName,columnName,value};
			
			formattedErrMsg = MessageFormat.format(Constants.OBJECT_NOT_FOUND_ERROR,arguments);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			formattedErrMsg = Constants.GENERIC_DATABASE_ERROR;  
		}
		return formattedErrMsg;
	}

	public static void main(String args[])
	{
		String formattedErrMsg=null;
		String temp1= "exists: ";
		String temp2= "class: ";
		String temp3= "given ";
		int startIndex = -1;
		int endIndex = -1;
		int tempIndex = -1;
		Connection connection=null;
		
		try
		{
			// get Message from exception object
			String message="No row with the given identifier exists: 123, of class: edu.wustl.catissuecore.domain.StorageContainer";

			
			// get column name from message
			startIndex = message.indexOf(temp1)-1;
			tempIndex=message.indexOf(temp3)+temp3.length();
			String columnName=message.substring(tempIndex,startIndex);
			//Logger.out.debug(columnName+"--"+columnName.length());
			
			// get column value on from message for which object was not found   
			startIndex = message.indexOf(temp1)+temp1.length();
			endIndex = message.indexOf(",",startIndex);
			String value = message.substring(startIndex,endIndex);
			//Logger.out.debug(value+"  "+value.length());
			
			// get class name from message
			startIndex = message.indexOf(temp2)+temp2.length();
			String className = message.substring(startIndex);
			//Logger.out.debug(className+"--"+className.length());
			Class classObj = Class.forName(className);
			// get table name from class 
			String displayName = "dispname";//getDisplayName(HibernateMetaData.getTableName(classObj),connection);
			
			Object[] arguments = new Object[]{displayName,columnName,value};
			
			formattedErrMsg = MessageFormat.format(Constants.OBJECT_NOT_FOUND_ERROR,arguments);
			Logger.out.debug(formattedErrMsg);
			}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			formattedErrMsg = Constants.GENERIC_DATABASE_ERROR;  
		}
		
		
	}
	
}
