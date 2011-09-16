
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATEPICKER" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DatePicker extends Control implements DatePickerInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String dateValueType = null;

	/**
	 * Empty Constructor
	 */
	public DatePicker()
	{
	}

	/**
	 * This method generates the HTML code for DatePicker control on the HTML form
	 * @return HTML code for DatePicker
	 * @throws DynamicExtensionsSystemException if couldn't genreate the HTML name for the Control.
	 */
	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		String defaultValue = (String) this.value;
		if (value == null)
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
			if (defaultValue == null)
			{
				defaultValue = "";
			}
		}

		String htmlComponentName = getHTMLComponentName();
        String output=null;
		/*String output = "<input class='"
				+ cssClass
				+ "' name='"
				+ htmlComponentName
				+ "' id='"
				+ htmlComponentName
				+ "' value='"
				+ defaultValue
				+ "'/>"
				+ "<A onclick=\"showCalendar('"
				+ htmlComponentName
				+ "', "
				+ DynamicExtensionsUtility.getCurrentYear()
				+ ", "
				+ DynamicExtensionsUtility.getCurrentMonth()
				+ ", "
				+ DynamicExtensionsUtility.getCurrentDay()
				+ ", 'MM-dd-yyyy', 'dataEntryForm', '"
				+ htmlComponentName
				+ "', event, 1900, 2020);\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
				+ "<DIV id=slcalcod"
				+ htmlComponentName
				+ " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";*/
        
        /*String output = "<input class='"
        + cssClass
        + "' name='"
        + htmlComponentName
        + "' id='"
        + htmlComponentName
        + "' value='"
        + defaultValue
        + "'/>"
        + "<A onclick=\"printMonthYearCalendar('"
        + htmlComponentName
        + "', "
        + DynamicExtensionsUtility.getCurrentMonth()
        + ", "
        + DynamicExtensionsUtility.getCurrentYear()
        + ");\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
        + "<DIV id=slcalcod"
        + htmlComponentName
        + " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";*/
        
        /*String output = "<input class='"
            + cssClass
            + "' name='"
            + htmlComponentName
            + "' id='"
            + htmlComponentName
            + "' value='"
            + defaultValue
            + "'/>"
            + "<A onclick=\"printMonthYearCalendar('"
            + htmlComponentName
            + "', "
            + DynamicExtensionsUtility.getCurrentMonth()
            + ", "
            + DynamicExtensionsUtility.getCurrentYear()
            + ");\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
            + "<DIV id=slcalcod"
            + htmlComponentName
            + " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";*/
		/* Obtain the date format */
		AttributeTypeInformationInterface attributeTypeInformationInterface = ((AttributeInterface) this
				.getAbstractAttribute()).getAttributeTypeInformation();
		String dateFormat = ControlsUtility.getDateFormat(attributeTypeInformationInterface);
		if (dateFormat.equals(ProcessorConstants.DATE_ONLY_FORMAT))
		{
            output = "<input class='"
                + cssClass
                + "' name='"
                + htmlComponentName
                + "' id='"
                + htmlComponentName
                + "' value='"
                + defaultValue
                + "'/>"
                + "<A onclick=\"showCalendar('"
                + htmlComponentName
                + "', "
                + DynamicExtensionsUtility.getCurrentYear()
                + ", "
                + DynamicExtensionsUtility.getCurrentMonth()
                + ", "
                + DynamicExtensionsUtility.getCurrentDay()
                + ", 'MM-dd-yyyy', 'dataEntryForm', '"
                + htmlComponentName
                + "', event, 1900, 2020);\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
                + "<DIV id=slcalcod"
                + htmlComponentName
                + " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";
			output += "<SCRIPT>printCalendar('" + htmlComponentName + "',"
					+ DynamicExtensionsUtility.getCurrentDay() + ","
					+ DynamicExtensionsUtility.getCurrentMonth() + ","
					+ DynamicExtensionsUtility.getCurrentYear() + ");</SCRIPT>" + "</DIV>"
					+ "[MM-DD-YYYY]&nbsp;";
		}
		else if (dateFormat.equals(ProcessorConstants.DATE_TIME_FORMAT))
		{
            output = "<input class='"
                + cssClass
                + "' name='"
                + htmlComponentName
                + "' id='"
                + htmlComponentName
                + "' value='"
                + defaultValue
                + "'/>"
                + "<A onclick=\"showCalendar('"
                + htmlComponentName
                + "', "
                + DynamicExtensionsUtility.getCurrentYear()
                + ", "
                + DynamicExtensionsUtility.getCurrentMonth()
                + ", "
                + DynamicExtensionsUtility.getCurrentDay()
                + ", 'MM-dd-yyyy', 'dataEntryForm', '"
                + htmlComponentName
                + "', event, 1900, 2020);\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
                + "<DIV id=slcalcod"
                + htmlComponentName
                + " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";
			output += "<SCRIPT>printTimeCalendar('" + htmlComponentName + "',"
					+ DynamicExtensionsUtility.getCurrentDay() + ","
					+ DynamicExtensionsUtility.getCurrentMonth() + ","
					+ DynamicExtensionsUtility.getCurrentYear() + ","
					+ DynamicExtensionsUtility.getCurrentHours() + ","
					+ DynamicExtensionsUtility.getCurrentMinutes() + ");</SCRIPT>" + "</DIV>"
					+ "[MM-DD-YYYY HH:MM]&nbsp;";
		}
        else if (dateFormat.equals(ProcessorConstants.MONTH_YEAR_FORMAT))
        {
            output = "<input class='"
                + cssClass
                + "' name='"
                + htmlComponentName
                + "' id='"
                + htmlComponentName
                + "' value='"
                + defaultValue
                + "'/>"
                + "<A onclick=\"showCalendar('"
                + htmlComponentName
                + "', "
                + DynamicExtensionsUtility.getCurrentYear()
                + ", "
                + DynamicExtensionsUtility.getCurrentMonth()
                + ", "
                + 0
                + ", 'MM-yyyy', 'dataEntryForm', '"
                + htmlComponentName
                + "', event, 1900, 2020);\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
                + "<DIV id=slcalcod"
                + htmlComponentName
                + " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";
            /*output = "<input class='"
                + cssClass
                + "' name='"
                + htmlComponentName
                + "' id='"
                + htmlComponentName
                + "' value='"
                + defaultValue
                + "'/>"
                + "<A onclick=\"printMonthYearCalendar('"
                + htmlComponentName
                + "', "
                + DynamicExtensionsUtility.getCurrentMonth()
                + ", "
                + DynamicExtensionsUtility.getCurrentYear()
                + ");\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
                + "<DIV id=slcalcod"
                + htmlComponentName
                + " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";*/
            output += "<SCRIPT>printMonthYearCalendar('" + htmlComponentName + "',"
            + DynamicExtensionsUtility.getCurrentMonth() + ","
            + DynamicExtensionsUtility.getCurrentYear()
            + ");</SCRIPT>" + "</DIV>"
            + "[MM-YYYY]&nbsp;";
        }
        else if (dateFormat.equals(ProcessorConstants.YEAR_ONLY_FORMAT))
        {
            /*output += "<SCRIPT>printYearCalendar('" + "attributeDefaultValue" + "',"
            + DynamicExtensionsUtility.getCurrentYear() + ","
            + ");</SCRIPT>" + "</DIV>"
            + "[YYYY]&nbsp;";
            output += "<SCRIPT>printYearCalendar('" + htmlComponentName + "',"
            + DynamicExtensionsUtility.getCurrentDay() + ","
            + DynamicExtensionsUtility.getCurrentMonth() + ","
            + DynamicExtensionsUtility.getCurrentYear() + ","
            + DynamicExtensionsUtility.getCurrentHours() + ","
            + DynamicExtensionsUtility.getCurrentMinutes() + ");</SCRIPT>" + "</DIV>"
            + "[MM-DD-YYYY HH:MM]&nbsp;";*/
            output = "<input class='"
                + cssClass
                + "' name='"
                + htmlComponentName
                + "' id='"
                + htmlComponentName
                + "' value='"
                + defaultValue
                + "'/>"
                + "<A onclick=\"showCalendar('"
                + htmlComponentName
                + "', "
                + DynamicExtensionsUtility.getCurrentYear()
                + ", "
                + 0
                + ", "
                + 0
                + ", 'yyyy', 'dataEntryForm', '"
                + htmlComponentName
                + "', event, 1900, 2020);\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 /></A>"
                + "<DIV id=slcalcod"
                + htmlComponentName
                + " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";
             output += "<SCRIPT>printYearCalendar('" + htmlComponentName + "',"
            + DynamicExtensionsUtility.getCurrentYear()
            + ");</SCRIPT>" + "</DIV>"
            + "[YYYY]&nbsp;";
        }

		return output;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface)
	{
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface#getDateValueType()
	 */
	public String getDateValueType()
	{
		return dateValueType;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface#setDateValueType(java.lang.String)
	 */
	public void setDateValueType(String dateValueType)
	{
		this.dateValueType = dateValueType;
	}

	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		String htmlString = "&nbsp;";
		if (value != null)
		{
			htmlString = "<span class = '" + cssClass + "'> " + this.value.toString() + "</span>";
		}

		return htmlString;
	}

}