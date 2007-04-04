/**
 * <p>Title: EntityCacheGetterInterface Class>
 * <p>Description:  This interface provides method for getting the EntityCache 
 * instance from the EntityCacheSessionBean.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.common.entityCache;

/**
 * This interface provides method for getting the EntityCache 
 * instance from the EntityCacheSessionBean.
 * @author gautam_shetty
 */
public interface EntityCacheGetterInterface
{
    /**
     * Returns the EntityCache instance form the EntityCacheSessionBean. 
     * @return the EntityCache instance form the EntityCacheSessionBean.
     */
    public IEntityCache getEntityCache();
}
