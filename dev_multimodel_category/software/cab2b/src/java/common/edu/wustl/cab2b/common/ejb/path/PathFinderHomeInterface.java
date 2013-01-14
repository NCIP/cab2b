/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.ejb.path;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Chandrakant Talele
 */
public interface PathFinderHomeInterface extends EJBHome {
    /**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */

    public PathFinderRemoteInterface create() throws RemoteException,CreateException;
}