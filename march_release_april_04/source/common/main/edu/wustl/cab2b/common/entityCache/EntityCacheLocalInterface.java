/**
 * <p>Title: EntityCacheLocalInterface Interface>
 * <p>Description:	Entity Cache remote interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.entityCache;

import javax.ejb.EJBLocalObject;

/**
 * Entity Cache remote interface.
 * @author gautam_shetty
 */
public interface EntityCacheLocalInterface extends EJBLocalObject, EntityCacheBusinessInterface, EntityCacheGetterInterface
{
}