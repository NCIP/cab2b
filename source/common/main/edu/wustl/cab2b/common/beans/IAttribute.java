/**
 * <p>Title: IAttribute Interface>
 * <p>Description:	IAttribute interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.beans;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

/**
 * IAttribute interface.
 * @author gautam_shetty
 */
public interface IAttribute extends AttributeInterface
{
    
//    /**
//     * @return Returns the createdDate.
//     */
//    public Date getCreatedDate();
//
//    /**
//     * @param createdDate The createdDate to set.
//     */
//    public void setCreatedDate(Date createdDate);
//    
//    /**
//     * @return Returns the description.
//     */
//    public String getDescription();
//    
//    /**
//     * @param description The description to set.
//     */
//    public void setDescription(String description);
//    
//    /**
//     * @return Returns the id.
//     */
//    public Long getId();
//    	
//    /**
//     * @param id The id to set.
//     */
//    public void setId(Long id);
//    
//    /**
//     * @return Returns the lastUpdated.
//     */
//    public Date getLastUpdated();
//    
//    /**
//     * @param lastUpdated The lastUpdated to set.
//     */
//    public void setLastUpdated(Date lastUpdated);
//    
//    /**
//     * @return Returns the name.
//     */
//    public String getName();
//    
//    /**
//     * @param name The name to set.
//     */
//    public void setName(String name);
//    
//    /**
//     * @return Returns the semanticPropertyCollection.
//     */
//    public Collection getSemanticPropertyCollection();
    
    /**
     * @param semanticPropertyCollection The semanticPropertyCollection to set.
     */
    public void setSemanticPropertyCollection(Collection semanticPropertyCollection);
    
//    /**
//     * @return Returns the ruleCollection.
//     */
//    public Collection getRuleCollection();
//
//    /**
//     * @param ruleCollection The ruleCollection to set.
//     */
//    public void setRuleCollection(Collection ruleCollection);
//
//    /**
//     * @return Returns the type.
//     */
//    public String getType();
    
    public void setAttributeTypeInformation(AttributeTypeInformationInterface attributetypeinformationinterface);
    
    /**
     * Returns true if the passed object obj is equal to this object else returns false. 
     * @param obj The object obj.
     * @return true if the passed object obj is equal to this object else returns false.
     */
    public boolean newEquals(Object obj);
}
