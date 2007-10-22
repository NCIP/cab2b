package edu.wustl.cab2b.common.ejb.datacategory;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface DataCategoryHomeInterface extends EJBHome{
    /**
     * This method creates the EJB Object.
     * @return The newly created EJB Object.
     */

    public DataCategoryRemoteInterface create() throws RemoteException,CreateException;

}
