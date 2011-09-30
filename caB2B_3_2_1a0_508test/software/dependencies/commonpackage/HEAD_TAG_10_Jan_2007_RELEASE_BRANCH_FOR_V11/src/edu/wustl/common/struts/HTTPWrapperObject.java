/**
 * <p>Title: HTTPWrapperObject Class>
 * <p>Description:	This class provides a wrapper object which constitutes
 * an object of AbstractDomainObject & operation that is to be performed.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 20, 2005
 */

package edu.wustl.common.struts;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.factory.AbstractActionFormFactory;
import edu.wustl.common.factory.MasterFactory;

/**
 * This class provides a wrapper object which constitutes
 * an object of AbstractDomainObject & operation that is to be performed.
 * @author Aniruddha Phadnis
 */
public class HTTPWrapperObject implements Serializable
{
    private static final long serialVersionUID = -4958330782397508598L;
    private ActionForm formBean;
	private String operation;
	
	public HTTPWrapperObject(){}
	
	public HTTPWrapperObject(Object domainObject,String operation) throws Exception
	{
	    //Gautam: Changes done for common package.
	    AbstractActionFormFactory actionFormFactory = (AbstractActionFormFactory)
	    												MasterFactory.getFactory(
	    												  "edu.wustl.catissuecore.actionForm.ActionFormFactory");
		AbstractActionForm abstractForm = actionFormFactory.getFormBean(domainObject,operation);
		formBean = abstractForm;
		
		this.operation = operation;
	}
	
	/**
	 * Returns FormBean object
	 */
	public ActionForm getForm()
	{
	    return formBean;
	}
	
	/**
	 * Returns Operation value
	 */
	public String getOperation()
	{
	    return this.operation;
	}
}