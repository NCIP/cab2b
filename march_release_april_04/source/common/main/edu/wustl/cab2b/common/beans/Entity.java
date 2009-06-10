/**
 * <p>Title: Entity Class>
 * <p>Description:	This class represents the entity metadata.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.common.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.wustl.cab2b.common.util.Utility;

/**
 * This class represents the entity metadata.
 * @author gautam_shetty
 */
public class Entity extends edu.common.dynamicextensions.domain.Entity implements IEntity, Serializable
{
    
    private static final long serialVersionUID = 1234567890L;
    
    /**
     * @param semanticPropertyCollection The semanticPropertyCollection to set.
     */
    public void setSemanticPropertyCollection(
            Collection semanticPropertyCollection)
    {
        this.semanticPropertyCollection = semanticPropertyCollection;
    }
    
    /**
     * @param attributeCollection The attributeCollection to set.
     */
    public void setAttributeCollection(Collection attributeCollection)
    {
        this.abstractAttributeCollection = attributeCollection;
    }
    
    /**
     * Default Constructor.  
     */
    public Entity()
    {
        //        setAttributeCollection(null);
        //        setCreatedDate(null);
        //        setDescription(null);
        //        setEntityGroupCollection(null);
        //        setId(null);
        //        setLastUpdated(null);
        //        setName(null);
        //        setSemanticPropertyCollection(null);
        //        setTableProperties(null);
    }
    
    public boolean newEquals(Object obj)
    {
        boolean matchStatus = false;
        
        if (obj instanceof Entity)
        {
            IEntity patternEntity = (IEntity) obj;
            
            if (patternEntity.getName() != null && getName() != null)
            {
                String patternName = "*"+patternEntity.getName()+"*";
                String className = getName();
                String onlyClassName = className.substring(className.lastIndexOf(".")+1,className.length());
                matchStatus = Utility.compareRegEx(patternName, onlyClassName);
            }
            
            if (patternEntity.getDescription() != null && getDescription() != null)
            {
                String patternDescription = "*"+patternEntity.getDescription()+"*";
                matchStatus = matchStatus || Utility.compareRegEx(patternDescription, getDescription());
            }
            
            if (patternEntity.getSemanticPropertyCollection() != null && getSemanticPropertyCollection() != null)
            {
                Iterator iterator = patternEntity
                        .getSemanticPropertyCollection().iterator();
                while (iterator.hasNext())
                {
                    SemanticProperty sourceSemanticProperty = (SemanticProperty) iterator
                            .next();
                    Iterator itr = getSemanticPropertyCollection().iterator();
                    while (itr.hasNext())
                    {
                        SemanticProperty targetSemanticProperty = (SemanticProperty) itr.next();
                        matchStatus = matchStatus
                                || targetSemanticProperty
                                        .newEquals(sourceSemanticProperty);
                    }
                }
            }
        }
        
        return matchStatus;
    }
}
