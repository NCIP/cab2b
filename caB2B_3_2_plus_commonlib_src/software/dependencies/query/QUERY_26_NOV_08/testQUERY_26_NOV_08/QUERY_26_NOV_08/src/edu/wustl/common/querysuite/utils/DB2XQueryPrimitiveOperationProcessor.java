package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ArithmeticOperator;
import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.TimeInterval;


public class DB2XQueryPrimitiveOperationProcessor extends SQLPrimitiveOperationProcessor {

    public DB2XQueryPrimitiveOperationProcessor() 
    {
        super("YYYY-MM-DD" , "xs:dateTime");
    }

    @Override
    String getDateDiffString(String leftStr, String rightStr) {
        return numSecs(leftStr + " - " + rightStr);
    }

    private String numSecs(String s) {
    	return s;
    }

    @Override
    String dateToTimestamp(String s) 
    {
// If it is an attribute 
    	if(s.contains("$"))
    	{
    		s = "xs:dateTime(" + s + ")";
    	}
    	else
    	{
    		s = "xs:dateTime(\"" + s + "T00:00:00\")";
    	}
        return s;
    }

    @Override
    String getTimeOffsetOpString(String timeStr, String offsetStr, ArithmeticOperator operator) {
        return super.getResultString(timeStr, operator, offsetStr);
    }
    
    protected String modifyDateLiteral(IDateLiteral s) {
        return standardDateFormat(s);
    }
    
    protected String getIntervalString(String s, TimeInterval<?> timeInterval) {
    	return getTimeIntervalFormula(s, timeInterval);
    }
    
    private String getTimeIntervalFormula(String s, TimeInterval<?> timeInterval)
    {
    	
    	if(timeInterval.name().equals("Year"))
    	{
    		int k = Integer.valueOf(s) * 365;
    		return "xdt:dayTimeDuration(\"P" + k + "D\")";
    	}
    	if(timeInterval.name().equals("Month"))
    	{
    		int k = Integer.valueOf(s) * 30;
    		return "xdt:dayTimeDuration(\"P" + k + "D\")";
    	}
    	if(timeInterval.name().equals("Day"))
    	{
    		return "xdt:dayTimeDuration(\"P" + s + "D\")";
    	}
    	if(timeInterval.name().equals("Hour"))
    	{
    		return "xdt:dayTimeDuration(\"PT" + s + "H\")";
    	}
    	if(timeInterval.name().equals("Minute"))
    	{
    		return "xdt:dayTimeDuration(\"PT" + s + "M\")";
    	}
    	if(timeInterval.name().equals("Second"))
    	{
    		return "xdt:dayTimeDuration(\"PT" + s + "S\")";
    	}
    	if(timeInterval.name().equals("Week"))
    	{
    		int k = Integer.valueOf(s) * 7;
    		return "xdt:dayTimeDuration(\"P" + k + "D\")";	
    	}
    	if(timeInterval.name().equals("Quarter"))
    	{
    		int k = Integer.valueOf(s) * 91;
    		return "xdt:dayTimeDuration(\"P" + k + "D\")";
    	}
    	return "";
    }

}
