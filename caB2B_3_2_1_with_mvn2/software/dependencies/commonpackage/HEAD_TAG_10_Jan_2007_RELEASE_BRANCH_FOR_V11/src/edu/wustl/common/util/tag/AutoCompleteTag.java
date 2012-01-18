/*
 * Created on Feb 16, 2007
 */
package edu.wustl.common.util.tag;

/**
 * @author Santosh Chandak
 * JSP tag for Autocomplete feature. The body of this tag is executed once for every call of the tag
 * when the page is rendered. 
 * To use this tag, include AutocompleterCommon.jsp in your page
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.Constants;

public class AutoCompleteTag extends TagSupport
{
	/**
	 * version ID 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * property on which Autocompleter is to be applied
	 */
	private String property;

	/**
	 * Object containing values of the dropdown. Supported datatypes 
	 * 1. String Array 2. List of Name Value Beans
	 * 
	 */
	private Object optionsList;

	/**
	 * Default style
	 */
	private String styleClass = "formFieldSized15";

	/**
	 * Number of results to be shown, set to 11 as for time dropdowns showing 11 values was more logical 
	 */
	private String numberOfResults = "11";

	/**
	 * Trigger matching when user enters these number of characters
	 */
	private String numberOfCharacters = "1";

	/**
	 * set to true if the textbox is readOnly
	 */
	private Object readOnly = "false";
	
	/**
	 * set to true if the textbox is disabled
	 */
	private Object disabled = "false";
		
	/**
	 * Functions to be called when textbox loses focus
	 */
	private String onChange = "";

	/**
	 * initial value in the textbox, this is compulsary attribute
	 */
	private Object initialValue = "";

	/**
	 * if the property is dependent on some other property 
	 * eg. type depends on class
	 * 
	 */
	private String dependsOn = "";

	/**
	 * size
	 */
	private String size = "300";
	
	/**
	 *  true - in case of static lists eg. class, type
	 *  false - in case of dynamic lists eg. site, user
	 */
	private String staticField = "true";
	
	/**
	 * A call back function, which gets executed by JSP runtime when opening tag
	 * for this custom tag is encountered.
	 */
	public int doStartTag() throws JspException
	{
						
		try {
			JspWriter out = pageContext.getOut();
			String autocompleteHTMLStr=null;
			if(staticField.equalsIgnoreCase("true"))
			{
			autocompleteHTMLStr = getAutocompleteHTML();
			}
			else
			{
				autocompleteHTMLStr = getAutocompleteHTMLForDynamicProperty();
			}
			
			
			  /**
			    *  Clearing the variables
			    */
			   onChange = "";
			   initialValue = "";
			   readOnly = "false";
			   dependsOn = "";
			   size="300";
			   disabled="false"; 

			out.print(autocompleteHTMLStr);

		} catch (IOException ioe) {
			throw new JspTagException("Error:IOException while writing to the user");
		}

		return SKIP_BODY;
	}
	
//	@SuppressWarnings("unchecked")
	private String getAutocompleteHTML() {
	    String autoCompleteResult = "";
	    
	    prepareCommonData();
		
		/**
		 *  Always pass the function with brackets, appending '()' will not be done
		 */
	
			if(onChange.equals(""))
			{
				onChange = "trimByAutoTag(this)";
			}
			else 
			{
				onChange = "trimByAutoTag(this);" + onChange;
			}
	    String div = "divFor" + property; 
	    autoCompleteResult += "<div id=\"" + div + "\" style=\"display: none;\" class=\"autocomplete\">";
	    autoCompleteResult += "</div>";
	    autoCompleteResult += "<input type=\"text\" class=\"" + styleClass + "\" value=\"" + initialValue + "\"size=\"" + size + "\" id=\"" + property + "\" name=\"" + property + "\"" + "onmouseover=\"showTip(this.id)\" onmouseout=\"hideTip(this.id)\"";
	  	   
	    if (readOnly.toString().equalsIgnoreCase("true"))   
	    {
			autoCompleteResult += "readonly";
		} else 
		{
			autoCompleteResult += "onblur=\"" + onChange + "\"";
		}
	  
	    autoCompleteResult += "/>";
	    String nameOfArrow = property + "arrow"; 
	    autoCompleteResult += "<image id='" + nameOfArrow + "' src='images/autocompleter.gif' alt='Click' width='18' height='19' hspace='0' vspace='0' align='absmiddle' />";
	    autoCompleteResult += "<script> var valuesInList = new Array();"; 
	    
	    if (optionsList instanceof List) {
			List nvbList = (List) optionsList;
			if (nvbList != null && nvbList.size() > 0) {
				
		    			// TODO other than NVB
						for (int i = 0; i < nvbList.size(); i++) {
						NameValueBean nvb = (NameValueBean) nvbList.get(i);
						autoCompleteResult += "valuesInList[" + i + "] = \""
								+ nvb.getName() + "\";";
					}
									
				}
		
		}
	    
	        if(property.equals(Constants.SPECIMEN_TYPE))
			 autoCompleteResult += "var AutoC = ";
			 autoCompleteResult += "new Autocompleter.Combobox(\"" + property + "\",\"" + div + "\",\"" + nameOfArrow + "\"" + ",valuesInList,  { tokens: new Array(), fullSearch: true, partialSearch: true,defaultArray:" + "valuesInList" + ",choices: " + numberOfResults + ",autoSelect:true, minChars: "+ numberOfCharacters +" });";
	     
	   autoCompleteResult += "</script>";	  
	    
       return autoCompleteResult;
	}
	
	//@SuppressWarnings("unchecked")
	private void prepareCommonData() {
		if(initialValue == null || initialValue.equals(""))
	    {
	    	initialValue = pageContext.getRequest().getParameter(property);
	    	
	    	 if(initialValue == null || initialValue.equals(""))
	 	    {
	    	  String[] title = (String[])
	    	  pageContext.getRequest().getParameterValues(property);
	    	  
	    	  if (title != null && title.length > 0) { if (title[0] != null) {
	    	  initialValue = title[0]; } }
	    	 
	 	    } 
	    }
		if (initialValue == null) {
			initialValue = "";
		}
		
			
		/**
		 *  As Type depends on class, get the optionsList as List from optionsList which was passed as a map
		 */
		if(property.equalsIgnoreCase(Constants.SPECIMEN_TYPE) && dependsOn!=null && !dependsOn.equals(""))
		{
			 String className = dependsOn;
			 List specimenTypeList = (List) ((Map)optionsList).get(className);
			 optionsList = specimenTypeList;
	    } 
		
		/**
		 *  Converting other data types to list of Name Value Beans
		 */
		
		if (optionsList instanceof String[]) {
			String[] stringArray = (String[]) optionsList;
			List tempNVBList = new ArrayList();
			if(stringArray!=null)
			{
				for(int i=0;i<stringArray.length;i++)
				{
					tempNVBList.add(new NameValueBean(stringArray[i],stringArray[i]));
				}
			}
			optionsList = tempNVBList;
		}
		
		
		if (optionsList instanceof List) { 
			
			List nvbList = (List) optionsList;
			if (nvbList != null && nvbList.size() > 0) {
				
					// TODO other than NVB
					NameValueBean nvb1 = (NameValueBean) nvbList.get(0);
					if (nvb1.getName().equals(Constants.SELECT_OPTION)) {
						nvbList.remove(0);
					}
					
			} 
			/* if(nvbList == null || nvbList.size() == 0)
			{
				initialValue = "No Records Present"; // needed?
			}  */
	} 
	
	}

	/**
	 * This function prepares the HTML to be rendered
	 * @return String - containing HTML to be rendered
	 * 
	 */
	//@SuppressWarnings("unchecked")
	private String getAutocompleteHTMLForDynamicProperty() {
	    String autoCompleteResult = "";
	    String displayProperty = "display" + property;
	    prepareCommonData();
	
		/**
		 *  Always pass the function with brackets, appending '()' will not be done
		 */
	
		if(onChange.equals(""))
		{
			onChange = "trimByAutoTagAndSetIdInForm(this)";
		}
		else
		{
			onChange = "trimByAutoTagAndSetIdInForm(this);" + onChange;
		}
		String name = "";
		if(initialValue.equals("0") || initialValue.toString().equalsIgnoreCase("undefined") || initialValue.equals(""))
		{
			name = pageContext.getRequest().getParameter(displayProperty);
	    	
	    	 if(name == null || name.equals(""))
	 	    {
	    	  String[] title = (String[])
	    	  pageContext.getRequest().getParameterValues(displayProperty);
	    	  
	    	  if (title != null && title.length > 0) { if (title[0] != null) {
	    		  name = title[0]; } }
	    	 
	 	    } 
		}
		
		if(name == null)
		{
			name = "";
		}
		String value = "";
		if (optionsList instanceof List) { 
			
			List nvbList = (List) optionsList;
					
			for(int i=0;i<nvbList.size();i++)
			{
				NameValueBean nvb1 = (NameValueBean) nvbList.get(i);
				if(nvb1.getValue().equals(initialValue) || nvb1.getValue()!=null && nvb1.getValue().equals("00") && initialValue.equals("0"))
				{
					name = nvb1.getName();
					value = nvb1.getValue();
					break;
				}
			}
			
	} 
		  
		
	    String div = "divFor" + displayProperty; 
	    autoCompleteResult += "<div id=\"" + div + "\" style=\"display: none;\" class=\"autocomplete\">";
	    autoCompleteResult += "</div>";
	    autoCompleteResult += "<input type=\"text\" class=\"" + styleClass + "\" value=\"" + name + "\"size=\"" + size + "\" id=\"" + displayProperty + "\" name=\"" + displayProperty + "\"" + "onmouseover=\"showTip(this.id)\" onmouseout=\"hideTip(this.id)\"";
	      
	    if (readOnly.toString().equalsIgnoreCase("true")) 
	    {
			autoCompleteResult += "readonly";
		} 

	    autoCompleteResult += " onblur=\"" + onChange + "\"";
	    if (disabled.toString().equalsIgnoreCase("true")) 
	    {
			autoCompleteResult += "disabled=\"true\"";
		}
	    
	    autoCompleteResult += "/>";
	    String nameOfArrow = property + "arrow";
	    autoCompleteResult += "<image id='" + nameOfArrow + "' src='images/autocompleter.gif' alt='Click' width='18' height='19' hspace='0' vspace='0' align='absmiddle'/>";
	    autoCompleteResult += "<input type=\"hidden\" id=\"" + property + "\" name=\"" + property + "\"value=\"" + value + "\"/>";
	    autoCompleteResult += "<script> var valuesInListOf" + displayProperty  +  " = new Array();";
	    autoCompleteResult += "var idsInListOf" + displayProperty  +  " = new Array();";
	 	
	     if (optionsList instanceof List) {
			List nvbList = (List) optionsList;
			if (nvbList != null && nvbList.size() > 0) { 
				
		    			for (int i = 0; i < nvbList.size(); i++) {
						NameValueBean nvb = (NameValueBean) nvbList.get(i);
						autoCompleteResult += "valuesInListOf" + displayProperty  +  "[" + i + "] = \""
								+ nvb.getName() + "\";";
						autoCompleteResult += "idsInListOf" + displayProperty + "[" + i + "] = \""
						+ nvb.getValue() + "\";";
					}
						/**
						 *  Giving call to autocompleter constructor
						 */
						 autoCompleteResult += "new Autocompleter.Combobox(\"" + displayProperty + "\",\"" + div + "\",\"" + nameOfArrow + "\"" + ",valuesInListOf" + displayProperty  +  ",  { tokens: new Array(), fullSearch: true, partialSearch: true,defaultArray:" + "valuesInListOf" + displayProperty  + ",choices: " + numberOfResults + ",autoSelect:true, minChars: "+ numberOfCharacters +" });";
						 // autoCompleteResult += "new Autocompleter.Combobox(\"" + property + "\",\"" + div + "\",'nameofarrow',valuesInList,  { tokens: new Array(), fullSearch: true, partialSearch: true,defaultArray:" + "valuesInList" + ",choices: " + numberOfResults + ",autoSelect:true, minChars: "+ numberOfCharacters +" });";
				}
		}
	     
	   autoCompleteResult += "</script>";	  
	 	   
       return autoCompleteResult;
	} 

		
	/**
     * A call back function
     */
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}

	/**
	 * @return
	 */
	public String getNumberOfCharacters() {
		return numberOfCharacters;
	}

	/**
	 * @param numberOfCharacters
	 */
	public void setNumberOfCharacters(String numberOfCharacters) {
		this.numberOfCharacters = numberOfCharacters;
	}

	/**
	 * @return
	 */
	public String getNumberOfResults() {
		return numberOfResults;
	}

	/**
	 * @param numberOfResults
	 */
	public void setNumberOfResults(String numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

	/**
	 * @return
	 */
	public Object getOptionsList() {
		return optionsList;
	}

	/**
	 * @param optionsList
	 */
	public void setOptionsList(Object optionsList) {
		this.optionsList = optionsList;
	}

	/**
	 * @return
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @param styleClass
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public Object getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(Object initialValue) {
		if (initialValue != null) {
			this.initialValue = initialValue.toString();
		}
	}

	/**
	 * @return Returns the onChange.
	 */
	public String getOnChange() {
		return onChange;
	}

	/**
	 * @param onChange The onChange to set.
	 */
	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	/**
	 * @return Returns the readOnly.
	 */
	public Object getReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(Object readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return Returns the dependsOn.
	 */
	public String getDependsOn() {
		return dependsOn;
	}

	/**
	 * @param dependsOn The dependsOn to set.
	 */
	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	/**
	 * @return Returns the staticField.
	 */
	public String getStaticField() {
		return staticField;
	}

	/**
	 * @param staticField The staticField to set.
	 */
	public void setStaticField(String staticField) {
		this.staticField = staticField;
	}

	/**
	 * @return Returns the disabled.
	 */
	public Object getDisabled() {
		return disabled;
	}

	/**
	 * @param disabled The disabled to set.
	 */
	public void setDisabled(Object disabled) {
		this.disabled = disabled;
	}
	
}