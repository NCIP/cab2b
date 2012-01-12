/**
 * <p>Title: AbstractDomain Class>
 * <p>Description:  AbstractDomain class is the superclass of all the domain classes. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.common.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.audit.Auditable;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Identifiable;


/**
 * AbstractDomain class is the superclass of all the domain classes.
 * @author gautam_shetty
 */
public abstract class AbstractDomainObject implements Auditable, Serializable,Identifiable
{
	
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 1234567890L;
	
	
	public String getObjectId() 
	{
		return this.getClass().getName()+ "_" + this.getId();
	}
    /**
     * Parses the fully qualified classname and returns only the classname.
     * @param fullyQualifiedName The fully qualified classname. 
     * @return The classname.
     */
    public static String parseClassName(String fullyQualifiedName)
    {
        try
        {
            return fullyQualifiedName.substring(fullyQualifiedName
                    .lastIndexOf(".") + 1);
        }
        catch (Exception e)
        {
            return fullyQualifiedName;
        }
    }
    
  //  public abstract void setAllValues(AbstractActionForm abstractForm) throws AssignDataException;
    /**
     * Copies all values from the AbstractForm object
     * @param abstractForm The AbstractForm object
     */
    public abstract void setAllValues(IValueObject  valueObject) throws AssignDataException;
    
    /**
	 * Returns the unique system identifier assigned to the domain object.
     * @return returns a unique system identifier assigned to the domain object.
     * @see #setId(Long)
	 * */
    public abstract Long getId();

    /**
	 * Sets an system Identifier for the domain object.
	 * @param system Identifier system Identifier for the domain object.
	 * @see #getId()
	 * */
    public abstract void setId(Long id);    
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
    public String getMessageLabel() {
    	return getId().toString();
    }
}