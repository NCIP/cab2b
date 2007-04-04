/**
 * <p>Title: AdvancedSearchHome interface>
 * <p>Description:	AdvancedSearchHome is the home interface for AdvancedSearchSessionBean.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */
package edu.wustl.cab2b.common.advancedSearch;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * AdvancedSearchHome is the home interface for AdvancedSearchSessionBean.
 * @author gautam_shetty
 */
public interface AdvancedSearchHome extends EJBHome
{
    /**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */
    public AdvancedSearchRemoteInterface create() throws RemoteException, CreateException;
}
