/**
 * <p>Description:	This class is used to cache the Entity and its Attribute objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.server.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.entityCache.EntityCacheBusinessInterface;
import edu.wustl.cab2b.common.entityCache.IEntityCache;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This class is used to cache the Entity and its Attribute objects.
 * @author gautam_shetty
 */
public class EntityCacheSessionBean extends AbstractStatelessSessionBean implements EntityCacheBusinessInterface, Serializable
{
    private static final long serialVersionUID = 1234567890L;
    
    private SessionContext sessionContext;
    
    /**
     * List of all Entity objects.
     */
    private static EntityCache entityCache;
    
    /**
     * Is set to true if the entity cache is loaded else is set to false.
     */
    static boolean cacheLoaded = false;
    /**
     * Returns the EntityCache instance.
     * @return Returns the entityCache.
     */
    public IEntityCache getEntityCache()
    {
        return entityCache;
    }
    
    /**
     * Sets the EntityCache instance.
     * @param entityCache The entityCache to set.
     */
    public static void setEntityCache(EntityCache entityCache)
    {
        EntityCacheSessionBean.entityCache = entityCache;
    }
    
    /**
     * @return Returns the cacheLoaded.
     */
    public static boolean isCacheLoaded()
    {
        return cacheLoaded;
    }
    
    /**
     * @param cacheLoaded The cacheLoaded to set.
     */
    public static void setCacheLoaded(boolean cacheLoaded)
    {
        EntityCacheSessionBean.cacheLoaded = cacheLoaded;
    }
    
    /**
     * Returns the Entity objects whose fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getEntityOnEntityParameters(Collection entityCollection)
    {
        return entityCache.getEntityOnEntityParameters(entityCollection);
    }
    
    /**
     * Returns the Entity objects whose Attribute fields match with the respective not null 
     * fields in the passed Attribute object.
     * @param entity The entity object.
     * @return the Entity objects whose Attribute fields match with the respective not null 
     * fields in the passed Attribute object.
     */
    public MatchedClass getEntityOnAttributeParameters(Collection attributeCollection)
    {
        return entityCache.getEntityOnAttributeParameters(attributeCollection);
    }
    
    /**
     * (non-Javadoc)
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext sessionContext) throws EJBException,
            RemoteException
    {
        this.sessionContext = sessionContext;
        
        // Get instance of the EntityCache class.
        entityCache = EntityCache.getInstance();
        
//        try
//        {
//            // If cache is not loaded, load the cache.
//            if (!isCacheLoaded())
//            {
//                entityCache.init();
//                cacheLoaded = true;
//            }
//        }
//        catch (RuntimeException runExp)
//        {
//            throw new RemoteException(runExp.getMessage(), runExp);
//        }
    }
}
