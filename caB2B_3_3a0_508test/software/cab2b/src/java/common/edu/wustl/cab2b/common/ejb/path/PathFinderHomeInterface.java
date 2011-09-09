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