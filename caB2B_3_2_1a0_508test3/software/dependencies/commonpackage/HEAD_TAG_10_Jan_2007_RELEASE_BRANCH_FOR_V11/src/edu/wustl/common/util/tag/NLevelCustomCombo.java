/*
 * Created on Jun 12, 2006
 */
package edu.wustl.common.util.tag;

/**
 * @author chetan_bh
 * JSP tag for N-level combo-box. The body of this tag is executed once per
 * every request for one n-level comboboxes. 
 */

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.common.beans.NameValueBean;

public class NLevelCustomCombo extends TagSupport
{
	/**
     * a datastructure of type Map of Map of ... of List.
     * This forms a hierarchy of data. The depth of this tree hierarchy decides
     * the number of combos used to display this complex data structure.
     * Hence it can be called a recursive Map data structure.
     */
	private Map dataMap;
	
	/**
     * List of string values used as labels for n combos.
     */
	private String[] labelNames;						// optional
	
	/**
     * List of string values used for naming each combos.
     */
	private String[] attributeNames;
	
	/**
     * List of string values used for naming each combos.
     */
	private String[] tdStyleClassArray;
	
	/**
     * List of string values used to initailize first n-1 combos.
     */
	private String[] initialValues;
	
	/**
     * A string value used to give, a row of combos a unique value for id attribute of each combo.
     */
	private String rowNumber;
	
	/**
	 * Number of empty combos needed for inital condition.
	 */
	private String noOfEmptyCombos = "3";
	
	/**
     * A string value representing the style class to use with all combos.
     */
	private String styleClass;						// optional
	
	/**
     * A string value representing the td style class to use with all table divisions.
     */
	protected String tdStyleClass;						// optional
	
	/**
     * A boolean value for getting combos vertically aligned.
     */
	private boolean isVerticalCombos;				// optional

	/**
	 * A boolean value to disable all the combos. 
	 */
	private boolean disabled;
	
	/**
	 * A string value for onChange event, common to all comboboxes.
	 */			
	private String onChange = "onCustomListBoxChange(this) ";
	
	/**
	 *  A string variable used to construct HTML for the n-level combo.
	 */
	private String combosHTMLStr		= "";
	
	/**
	 * An integer counter variable for counting combos. 
	 */
	private int comboCounter			= 0;
	
	/**
	 * A vriable to store the number of combos needed to construct n-combos.
	 */
	private int numberOfCombosNeeded	= 0;
	
	private String formLabelStyle = "";
	
	/**
	 * A string vriable which stores opening table row tag <tr> for 
	 * vertical combos, otherwise empty for horizantal combos.
	 */
	private String verticalCombosStart	= "";
	
	/**
	 * A string vriable which stores closing table row tag </tr> for 
	 * vertical combos, otherwise empty for horizantal combos.
	 */
	private String verticalCombosEnd	= "";
	
	/**
     * A call back function, which gets executed by JSP runtime when opening tag for this
     * custom tag is encountered. Call a recursive function to get the work done.
     */
	public int doStartTag() throws JspException
	{
		numberOfCombosNeeded();
		initOptionalAttributes();
		if( validate() == false )
		{
			System.out.println("invalid tag attributes -> "+numberOfCombosNeeded);
			return SKIP_BODY;
		}
		
		try {
			JspWriter out = pageContext.getOut();
			
			combosHTMLStr = combosHTMLStr + "<table cellpadding='0' cellspacing='0'><tr>";
			getCombos(dataMap);

			out.print(combosHTMLStr);
				
		} catch(IOException ioe) {
				throw new JspTagException("Error:IOException while writing to the user");
		}

		// has to be reintialized for next usage of this tag.
		comboCounter = 0;
		numberOfCombosNeeded = 0;
		combosHTMLStr ="";
		verticalCombosStart = "";
		verticalCombosEnd = "";
		return SKIP_BODY;
	}
	
	/**
	 * A validation function.
	 * @return true if tag attributes are valid, false if tag attributes are invalid.
	 */
	private boolean validate()
	{
		ServletRequest request = pageContext.getRequest(); 
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		
		if(errors == null)
		{
			errors = new ActionErrors();
		}
		//System.out.println("numberOfCombosNeeded "+numberOfCombosNeeded);
		if(attributeNames == null || attributeNames.length != numberOfCombosNeeded )
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Attributes list is either null or size doesn't match"));
			request.setAttribute(Globals.ERROR_KEY,errors);
			//System.out.println("Attributes list is either null or size doesn't match");
			return false;
		}else if (labelNames == null || labelNames.length != numberOfCombosNeeded)
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Label names list is either empty or size doesn't match"));
			request.setAttribute(Globals.ERROR_KEY,errors);
			//System.out.println("Label names list is either empty or size doesn't match");
			return false;
		}else if(initialValues == null ||  initialValues.length > numberOfCombosNeeded || initialValues.length < (numberOfCombosNeeded-1))
		{
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("Initail values list is either empty or size doesn't match"));
			request.setAttribute(Globals.ERROR_KEY,errors);
			//System.out.println("Initail values list is either empty or size doesn't match");
			return false;
		}
		
		return true;
	}
	
	/**
     * A call back function to set a recursive Map data structure 'dataMap'.
     * @param value
     */
	public void setDataMap(Map value) {
		dataMap = value;
	}
	
//	private Map getNLevelEmptyMaps(int n)
//	{
//		Map returner = new TreeMap();
//		Map currentMap = returner;
//		for(int i =0 ; i< n-1; i++)
//		{
//			Map map = new TreeMap();
//			currentMap.put("0",map);
//			currentMap = map;
//		}
//		currentMap.put("0",new Vector());
//		return returner;
//	}
	
	/**
	 * A call back function which sets onChange.
	 * @param value
	 */
	public void setOnChange(String value) {
		onChange = value;
	}
	
	/**
     * A call back function to set a list of label names.
     * @param value
     */
	public void setLabelNames(String[] value) {
		labelNames = value;
	}
	
	/**
     * A call back function to set a list of attribute names.
     * @param value
     */
	public void setAttributeNames(String[] value) {
		attributeNames = value;
	}
	
	/**
     * A call back function to set a list of initialization values.
     * @param value
     */
	public void setInitialValues(String[] value) {
		//System.out.println("initValues "+value);
		//print(value);
		initialValues = value;
	}
	
	public  String[] getInitalValues()
	{
		return initialValues;
	}
	
	/**
     * A call back function to set row number.
     * @param value
     */
	public void setRowNumber(String value) {
		rowNumber = value;
	}
	
	/**
     * A call back function to set  style class. 
     * @param value
     */
	public void setStyleClass(String value) {
		styleClass = value;
	}
	
	/**
     * A call back function to set td style class. 
     * @param value
     */
	public void setTdStyleClass(String value) {
		tdStyleClass = value;
	}
	
	/**
     * A call back function to set isVerticalCombos. 
     * @param value
     */
	public void setIsVerticalCombos(boolean value) {
		isVerticalCombos = value;
		if(isVerticalCombos == true)
		{
			verticalCombosStart = "<tr>";
			verticalCombosEnd = "</tr>";
		}
	}
	
	/**
	 * A call back function to set noOfEmptyCombos.
	 * @param value The noOfEmptyCombos to set.
	 */
	public void setNoOfEmptyCombos(String value) {
		this.noOfEmptyCombos = value;
	}
	
	/**
     * A initialization function which takes care of
     * initialization related to optional attributes.
     */
	private void initOptionalAttributes()
	{
		if(labelNames == null || labelNames.length == 0)
		{
			labelNames = new String[numberOfCombosNeeded];
			for(int i = 0; i <= numberOfCombosNeeded; i++)
			{
				labelNames[i] = "";
			}
		}
		if(initialValues == null || initialValues.length == 0)
		{
			initialValues = new String[numberOfCombosNeeded];
			for(int i = 0; i <= numberOfCombosNeeded; i++)
			{
				initialValues[i] = "-1";
			}
		}
		else
		{
			for(int i = 0; i < initialValues.length;i++)
			{
				if(initialValues[i] == null || initialValues[i].equals("") || initialValues[i].equals("0"))
				{
					initialValues[i] = "-1";
				}
			}
		}
		//System.out.println("--initvalue--");
		//print(initialValues);
		if(styleClass == null)
		{
			styleClass = "";
		}
		if(tdStyleClass == null)
		{
			tdStyleClass = "";
		}
		if(rowNumber == null || rowNumber.equals(""))
		{
			rowNumber = "1";
		}
	}
	
	/**
     * A utility function to find nunber of combos needed from a recursive map data structure. 
     */
	private void numberOfCombosNeeded()
	{
	  if(dataMap.size() > 0)
	  {
		Map map = dataMap;
		while(map instanceof Map)
		{
			numberOfCombosNeeded++;
			Set kSet = map.keySet();
			Object[] kSetArray = kSet.toArray();
			Object firstVal = map.get(kSetArray[0]);
			if(firstVal instanceof Map)
			{				
				map = (Map)firstVal;
			}else
			{
				numberOfCombosNeeded++;
				break;
			}
		}
	  }else
	  {
	  	numberOfCombosNeeded = Integer.parseInt(noOfEmptyCombos);
	  }
	}
	
	/**
     * A recursive function to construct the html code for n-level combos.
     * Number of calls to this function is equivalent to number of combos created. 
     * @param dMap - a 
     */
	private void getCombos(Object dMap)
	{
		if(tdStyleClassArray != null)
		{
			tdStyleClass = tdStyleClassArray[comboCounter];
		}
	  if( (dMap instanceof Map && ((Map)dMap).size() > 0 ) || (dMap instanceof List) && (((List)dMap).size() > 0) )
	  {
		if(dMap instanceof Map)
		{
			Set keySet = ((Map)dMap).keySet();
			combosHTMLStr = combosHTMLStr + verticalCombosStart + "<td class=\""+formLabelStyle+"\">"+"</td><td class=\""+formLabelStyle+"\" nowrap> "
												+"<select size=\"1\" name =\""+attributeNames[comboCounter]
												+"\" style =\""+styleClass
												+"\" onmouseover =\"showTip(this.id)\""
												+"\" onmouseout =\"hideTip(this.id)\""
												+"\" class=\""+tdStyleClass
												+"\" id =\"customListBox_"+rowNumber+"_"+comboCounter
												+"\" onChange=\""+onChange+"\"";
												if(disabled)
												{
													combosHTMLStr += " disabled = \"true\"";
												}												
												combosHTMLStr += " >";
			String initialValForThisCombo = (String)initialValues[comboCounter];
			
			combosHTMLStr = combosHTMLStr + "<option value=\"-1\">---</option>";		// added on 12-06-2006
			
			// iterate through keys to get options for the current combo.
			Iterator keyIter = keySet.iterator();
			//System.out.println("comboCounter "+comboCounter);
			
			if((comboCounter == 0) || (comboCounter > 0 && !(( (String)initialValues[comboCounter-1]).equals("-1")) ))
			{
				while(keyIter.hasNext())
				{
					// original String key = String.valueOf(keyIter.next());
					NameValueBean nvb=(NameValueBean)keyIter.next();
					String optionSelection = "";
					
					if(initialValForThisCombo.equals(nvb.getValue()))
					{
						optionSelection = " selected =\"true\" ";
					}
					combosHTMLStr = combosHTMLStr + "<option "+optionSelection+" value=\""+nvb.getValue()+"\">";
					combosHTMLStr = combosHTMLStr + nvb.getName();
					combosHTMLStr = combosHTMLStr + "</option>";
				}
			}
			
			combosHTMLStr = combosHTMLStr + "</select> </td> " + verticalCombosEnd;
			Object[] keySetArray = keySet.toArray();
			
			// logic to use init value starts here
			String initValue = (String) initialValues[comboCounter];
			int indexForNextCombo = 0;
			for(int i = 0; i < keySetArray.length; i++)
			{
				//String key = String.valueOf(keySetArray[i]);
				String key=((NameValueBean)keySetArray[i]).getValue();
				if(initValue.equals(key))
				{
					indexForNextCombo = i;
				}
			}
			
			comboCounter++;
			
			getCombos(((Map)dMap).get(keySetArray[indexForNextCombo]));			
			
		}else if(dMap instanceof List)      // Termination condition for recursion
		{
			List dList = (List) dMap;
			combosHTMLStr = combosHTMLStr + verticalCombosStart +"<td class=\""+formLabelStyle+"\">"+"</td><td class=\""+formLabelStyle+"\" nowrap> "
												+"<select size=\"1\" name =\""+attributeNames[comboCounter]
												+"\" style =\""+styleClass
												+"\" class=\""+tdStyleClass
												+"\" id =\"customListBox_"+rowNumber+"_"+comboCounter+"\"";
												if(disabled)
												{
													combosHTMLStr += " disabled = \"true\"";
												}												
												combosHTMLStr += " >";
			
			combosHTMLStr = combosHTMLStr + "<option value=\"-1\">---</option>";		// added on 12-06-2006
			//System.out.println("dList ---> "+dList);
			//print(initialValues);
			if( comboCounter > 0 && !(( (String)initialValues[comboCounter-1]).equals("-1")) )
			{
				for(int i = 0; i < dList.size(); i++ )
				{
					String optionSelection = "";
					NameValueBean nvb=(NameValueBean)dList.get(i);
					if(initialValues.length == numberOfCombosNeeded && initialValues[comboCounter] != null && !(initialValues[comboCounter].equals("")) )
					{					//if(initialValues.length == numberOfCombosNeeded && initialValues[i] != null && !(initialValues[i].equals("")) )
						String initialValForThisCombo = (String)initialValues[comboCounter];
						if(initialValForThisCombo.equals( nvb.getValue()))
						{
							optionSelection = " selected =\"true\" ";
						}
					}
					combosHTMLStr = combosHTMLStr + "<option "+optionSelection+"value=\""+nvb.getValue()+"\">";
					combosHTMLStr = combosHTMLStr + nvb.getName();
					combosHTMLStr = combosHTMLStr + "</option>";
				}
			}
			combosHTMLStr = combosHTMLStr + "</select> </td> " + verticalCombosEnd;
			comboCounter++;
			return;
		}
	  }else // to handle initial value condition were dMap.size() == 0 , NO RECURSSIVE CALL FROM HERE
	  {
	  	// use noOfEmptyCombos here
	  	
		for(int i = 0; i < Integer.parseInt(noOfEmptyCombos); i++ )
		{
			if(tdStyleClassArray != null)
			{
				tdStyleClass = tdStyleClassArray[comboCounter];
			}
			combosHTMLStr = combosHTMLStr + verticalCombosStart + "<td class=\""+formLabelStyle+"\" nowrap> "
									+"<select size=\"1\" name =\""+attributeNames[comboCounter]
									+"\" style =\""+styleClass
									+"\" class=\""+tdStyleClass
									+"\" id =\"customListBox_"+rowNumber+"_"+comboCounter+"\"";
									if(disabled)
									{
										combosHTMLStr += " disabled = \"true\"";
									}												
									combosHTMLStr += " >";
									
									combosHTMLStr = combosHTMLStr + "<option value=\"-1\">---</option>";		// added on 12-06-2006
									comboCounter++;
		}
	  }
	}
	
	private void print(String[] strArray)
	{
		System.out.println("-------------"+strArray.length+"-----------");
		for(int i = 0; i< strArray.length; i++)
		{
			System.out.println(i+" : "+strArray[i]);
		}
		System.out.println("----------------------------------------------");
	}
	
	/**
     * A call back function
     */
	public int doEndTag() throws JspException
	{
//		try {
//			JspWriter out = pageContext.getOut();
//			out.print("</tr>     </table>");
//		}catch(Exception io)
//		{
//			io.printStackTrace();
//		}
		return EVAL_PAGE;
	}

	/**
	 * Sets disabled.
	 * @param disabled The disabled to set.
	 */
	public void setDisabled(boolean value) {
		this.disabled = value;
	}

	/**
	 * @param formLabelStyle The formLabelStyle to set.
	 */
	public void setFormLabelStyle(String value) {
		this.formLabelStyle = value;
	}
	
	/**
	 * @return Returns the tdStyleClassArray.
	 */
	public String[] getTdStyleClassArray()
	{
		return tdStyleClassArray;
	}
	/**
	 * @param tdStyleClassArray The tdStyleClassArray to set.
	 */
	public void setTdStyleClassArray(String[] tdStyleClassArray)
	{
		this.tdStyleClassArray = tdStyleClassArray;
	}
}