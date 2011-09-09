
package edu.common.dynamicextensions.domaininterface;


/**
 * This is a primitive attribute of type date.
 * @author geetika_bangard
 */
public interface DateTypeInformationInterface extends AttributeTypeInformationInterface 
{

       
    /**
     * Date format for the date.The user input date will be validated against this format.
     * @return Returns the format.
     */
    String getFormat();
    /**
     * @param format The format to set.
     */
    void setFormat(String format);
	
}
