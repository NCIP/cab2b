package edu.wustl.cab2b.common.ejb.queryengine;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface QueryEngineHome extends EJBHome {
    /**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */
    public QueryEngineRemoteInterface create() throws RemoteException, CreateException;
}
