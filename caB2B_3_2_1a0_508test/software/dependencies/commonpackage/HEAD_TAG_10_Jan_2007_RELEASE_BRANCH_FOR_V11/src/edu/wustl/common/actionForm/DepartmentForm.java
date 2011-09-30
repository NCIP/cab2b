/**
 * <p>Title: DepartmentForm Class</p>
 * <p>Description:  DepartmentForm Class is used to handle transactions related to Department   
 * from Department Add/Edit webpage. </p>
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on May 23rd, 2005
 */

package edu.wustl.common.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.domain.Department;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * DepartmentForm Class is used to encapsulate all the request parameters passed 
 * from Department Add/Edit webpage.
 * @author Mandar Deshmukh
 * */
public class DepartmentForm extends AbstractActionForm
{
    /**
     * Name of the Department.
     */
    private String name;
    
    /**
     * No argument constructor for UserForm class 
     */
    public DepartmentForm()
    {
        reset();
    }

    /**
     * Copies the data from an AbstractDomain object to a DepartmentForm object.
     * @param Department An AbstractDomain object.  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        Department department = (Department)abstractDomain;
        this.id = department.getId().longValue();
        this.name = department.getName();
    }
    
        
    /**
     * Returns the name of the Department.
     * @return String representing the name of the Department
     */
    public String getName()
    {
        return (this.name);
    }

    /**
     * Sets the name of this Department
     * @param Name name of the Department.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.DEPARTMENT_FORM_ID;
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
        this.name = null;
    }

    /**
    * Overrides the validate method of ActionForm.
    * */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        try
        {
            if (validator.isEmpty(name))
            {
            	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("department.name")));
            }
        }
        catch(Exception excp)
        {
            Logger.out.error(excp.getMessage(),excp);
        }
        return errors;
     }
    
}