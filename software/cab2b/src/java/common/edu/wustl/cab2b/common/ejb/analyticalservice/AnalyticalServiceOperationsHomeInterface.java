/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.ejb.analyticalservice;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *  Home interface for AnalyticalServiceOperations
 * @author Chandrakant Talele
 */
public interface AnalyticalServiceOperationsHomeInterface extends EJBHome {
    /**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */

    public AnalyticalServiceOperationsRemoteInterface create() throws RemoteException,CreateException;
}
