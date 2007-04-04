/**
 * <p>Title: IEntity Interface>
 * <p>Description:	Entity interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.beans;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * Entity interface.
 * @author gautam_shetty
 */
public interface IEntity extends EntityInterface
{
    
    /**
     * @param semanticPropertyCollection The semanticPropertyCollection to set.
     */
    public void setSemanticPropertyCollection(Collection semanticPropertyCollection);
    
    /**
     * @param attributeCollection The attributeCollection to set.
     */
    public void setAttributeCollection(Collection attributeCollection);
    
    /**
     * Returns true if the passed object obj is equal to this object else returns false. 
     * @param obj The object obj.
     * @return true if the passed object obj is equal to this object else returns false.
     */
    public boolean newEquals(Object obj);
}