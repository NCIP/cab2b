package edu.wustl.cab2b.common.ejb.utility;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Chandrakant Talele
 */
public interface UtilityHomeInterface extends EJBHome {
    /**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */

    public UtilityRemoteInterface create() throws RemoteException,CreateException;
}