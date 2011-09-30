/**
 * <p>Title: SimpleQueryInterfaceAction Class>
 * <p>Description:	SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;

/**
 * SimpleQueryInterfaceAction initializes the fields in the Simple Query Interface.
 * @author gautam_shetty
 */
public class SimpleQueryInterfaceAction extends SecureAction
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;

        QueryBizLogic queryBizLogic = new QueryBizLogic();//(QueryBizLogic) BizLogicFactory.getBizLogic(simpleQueryInterfaceForm.getFormId());  
        HttpSession session = request.getSession();
        String operation = (String)request.getParameter(Constants.OPERATION);
        //Bug 1096:Setting the query map with the session object if the operation is redefining the query.
        if(operation!=null && operation.equals(Constants.REDEFINE))
    	{
        	Map simpleQueryMap = (Map)session.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT);
        	String originalCounter = (String)session.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER);
    		simpleQueryInterfaceForm.setValues(simpleQueryMap);
    		simpleQueryInterfaceForm.setCounter(originalCounter);
    	}
		//Set the session objects to null after it is set in the form.
		session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT,null);
		session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER,null);

        //Patch for Bug : 806
        String objectChanged = request.getParameter("objectChanged");
        
        if(objectChanged != null)
        {
        	Map map = simpleQueryInterfaceForm.getValues();
        	
        	//To fixed the bug which says Label/Title should be selected by default in search attibute drop down.
//        	if(map.containsKey(objectChanged))
//        	{
//        		String objectValue = (String)map.get(objectChanged);
//        		String newFieldValue = objectValue + ".IDENTIFIER.bigint";
//        		
//        		String fieldKey = objectChanged.replaceAll("table","field");
//        		map.put(fieldKey,newFieldValue);
//        	}
        }
        int counter = Integer.parseInt(simpleQueryInterfaceForm.getCounter());
        for (int i=1;i<=counter;i++)
        {
            // Key of present object.
            String key = "SimpleConditionsNode:"+i+"_Condition_DataElement_table";
            String value = (String)simpleQueryInterfaceForm.getValue(key);
            
            Validator validator = new Validator();
            if ((value != null) && (validator.isValidOption(value)))
            {
                List columnNameValueBeanList = queryBizLogic.getColumnNames(value);
                if (columnNameValueBeanList.isEmpty() == false)
                {
                    String attributeNameList = "attributeNameList"+i;
                    request.setAttribute(attributeNameList, columnNameValueBeanList);
                }
            }
            
            // For the last condition row.  
            if (i == counter) 
            {
                //Key of previous object.
                String prevKey = "SimpleConditionsNode:"+(i-1)+"_Condition_DataElement_table";
                String prevValue = (String)simpleQueryInterfaceForm.getValue(prevKey);
                
                //Mandar : code to hide the calendar icon
                String calKey = "SimpleConditionsNode:"+i+"_showCalendar";
                simpleQueryInterfaceForm.setShowCalendar(calKey,"");
                // Mandar : code complete
                
                //If previous table name is not null, get the list of table name related to it.
                if (prevValue != null)
                {
                    Set nextTableNameList = queryBizLogic.getNextTableNames(prevValue);
                    if (nextTableNameList.isEmpty() == false)
                    {
                        String objectNameList = "objectList"+i;
                        request.setAttribute(objectNameList, nextTableNameList);
                        request.setAttribute(Constants.ATTRIBUTE_NAME_LIST, Constants.ATTRIBUTE_NAME_ARRAY);
                    }
                }
                else//If there is only one condition row. 
                {
                    String aliasName = request.getParameter(Constants.TABLE_ALIAS_NAME);
                    
                    // Get all the table names.  
                    Set objectNameValueBeanList = queryBizLogic.getAllTableNames(aliasName, Constants.SIMPLE_QUERY_TABLES);
                    if (objectNameValueBeanList.isEmpty() == false)
                    {
                        request.setAttribute(Constants.OBJECT_NAME_LIST, objectNameValueBeanList);
                        request.setAttribute(Constants.ATTRIBUTE_NAME_LIST, Constants.ATTRIBUTE_NAME_ARRAY);
                    }
                    
                    if ((aliasName != null) && (!"".equals(aliasName)))
            		{
                        request.setAttribute(Constants.TABLE_ALIAS_NAME,aliasName);
                        
            		    List columnNameValueBeanList = queryBizLogic.getColumnNames(aliasName);
                        if (columnNameValueBeanList.isEmpty() == false)
                        {
                            String attributeNameList = "attributeNameList1";
                            request.setAttribute(attributeNameList, columnNameValueBeanList);
                        }
            		}
                }
            }
            else// For rows other than last, show only the object selected.
            {
                //Key of the next operator (AND/OR).
                String nextOperatorKey = "SimpleConditionsNode:"+i+"_Operator_operator";
                String nextOperatorValue = (String)simpleQueryInterfaceForm.getValue(nextOperatorKey);
                
                if (nextOperatorValue != null && !"".equals(nextOperatorValue))
                {
                    String objectNameValueBeanList = "objectList"+i;
                    String prevValueDisplayName = queryBizLogic.getDisplayName(value);
                    
                    NameValueBean nameValueBean = new NameValueBean();
                    nameValueBean.setName(prevValueDisplayName);
                    nameValueBean.setValue(value);
                    
                    List objectList = new ArrayList();
                    objectList.add(nameValueBean);
                    
                    request.setAttribute(objectNameValueBeanList, objectList);
                }
            }
        }
        
        request.setAttribute(Constants.ATTRIBUTE_CONDITION_LIST, Constants.ATTRIBUTE_CONDITION_ARRAY);
        
        //Initialize the session attributes to null
        session.setAttribute(Constants.SIMPLE_QUERY_MAP,null);
        session.setAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST,null);
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF, pageOf);
        
        return mapping.findForward(pageOf);
    }
    
    /**
     * Returns the object id of the protection element that represents
     * the Action that is being requested for invocation.
     * @param clazz
     * @return
     */
    protected String getObjectIdForSecureMethodAccess(HttpServletRequest request)
    {
        String aliasName = request.getParameter("aliasName");
        if(aliasName!=null && !aliasName.equals(""))
        {
            return this.getClass().getName()+"_"+aliasName;
        }
        else
        {
            return super.getObjectIdForSecureMethodAccess(request);
        }
    }
}