/**
 * <p>Title: EntityCacheHome interface>
 * <p>Description:	EntityCacheHome is the home interface for EntityCacheSessionBean.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.entityCache;

import javax.ejb.EJBLocalHome;

/**
 * This is the home interface for AdvancedSearchSessionBean. 
 * This interface is implemented by the EJB Server's tools - the implemented
 * object is called the Home Object, and serves as a factory for EJB Objects.
 * 
 * One create() method is in this Home Interface, 
 * which corresponds to the ejbCreate() method in AdvancedSearchSessionBean.
 * @author gautam_shetty
 */
public interface EntityCacheHome extends EJBLocalHome
{
    /**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */
    public EntityCacheLocalInterface create() throws javax.ejb.CreateException;
}
