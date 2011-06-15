package edu.common.dynamicextensions.domaininterface.userinterface;
/**
 * DatePickerInterface stores necessary information for generating DatePicker control on
 * dynamically generated user interface.  
 * @author geetika_bangard
 */
public interface DatePickerInterface extends ControlInterface
{

	/**
 	 * 
 	 * @return date value type as None/Today's date/selected date
 	 */
 	public String getDateValueType();
 	/**
 	 * 
 	 * @param dateValueType None/Today's date/selected date
 	 */
 	public void setDateValueType(String dateValueType);  
    
}
