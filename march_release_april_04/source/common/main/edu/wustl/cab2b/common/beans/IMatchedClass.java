package edu.wustl.cab2b.common.beans;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

public interface IMatchedClass
{
    /**
     * @return Returns the entityCollection.
     */
    public Set<EntityInterface> getEntityCollection();
    
    /**
     * @param entityCollection The entityCollection to set.
     */
    public void setEntityCollection(Set<EntityInterface> entityCollection);
    
    /**
     * @return Returns the matchedAttributeCollection.
     */
    public Set<AttributeInterface> getMatchedAttributeCollection();
    
    /**
     * @param matchedAttributeCollection The matchedAttributeCollection to set.
     */
    public void setMatchedAttributeCollection(Set<AttributeInterface> matchedAttributeCollection);
}
