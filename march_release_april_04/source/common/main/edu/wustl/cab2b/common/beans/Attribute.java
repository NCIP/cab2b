/**
 * <p>Title: Attribute Class>
 * <p>Description:	This class represents the attribute metadata.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.wustl.cab2b.common.util.Utility;

/**
 * This class represents the attribute metadata.
 * @author gautam_shetty
 */
public class Attribute extends edu.common.dynamicextensions.domain.Attribute implements IAttribute, Serializable
{
    
    private static final long serialVersionUID = 1234567890L;
    
    public void setSemanticPropertyCollection(Collection semanticPropertyCollection)
    {
        this.semanticPropertyCollection = semanticPropertyCollection;
    }
    
    public void setAttributeTypeInformation(AttributeTypeInformationInterface attributetypeinformationinterface)
    {
        super.setAttributeTypeInformation(attributetypeinformationinterface);
    }
    
    /**
     * Default constructor.
     */
    public Attribute()
    {
        setCreatedDate(null);
        setDescription(null);
        setId(null);
        setLastUpdated(null);
        setName(null);
        setSemanticPropertyCollection(null);
    }

    public boolean newEquals(Object obj)
    {
        boolean matchStatus = false;

        if (obj instanceof Attribute)
        {
            IAttribute patternAttribute = (IAttribute) obj;

            if (patternAttribute.getName() != null && getName() != null)
            {
                String patternName = "*"+patternAttribute.getName()+"*";
//                System.out.println("Pattern ............... " + patternName);
//                System.out.println("Entity Name..................." + getName());
                matchStatus = Utility.compareRegEx(patternName, getName());
//                System.out.println("Name Status..............." + matchStatus);
            }

            if (patternAttribute.getDescription() != null
                    && getDescription() != null)
            {
                String patternDescription = "*"+patternAttribute.getDescription()+"*";
                matchStatus = matchStatus || Utility.compareRegEx(patternDescription, getDescription());
//                System.out.println("Pattern ............... "+ patternDescription);
//                System.out.println("Entity Desc..................."+ getDescription());
//                System.out.println("Desc Status..............."+ matchStatus);
            }

            if (patternAttribute.getSemanticPropertyCollection() != null
                    && getSemanticPropertyCollection() != null)
            {
                Iterator iterator = patternAttribute
                        .getSemanticPropertyCollection().iterator();
                while (iterator.hasNext())
                {
                    SemanticProperty sourceSemanticProperty = (SemanticProperty) iterator
                            .next();
                    Iterator itr = getSemanticPropertyCollection().iterator();
                    while (itr.hasNext())
                    {
                        SemanticProperty targetSemanticProperty = (SemanticProperty) itr
                                .next();
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
