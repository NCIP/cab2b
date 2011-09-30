package edu.wustl.cab2b.common.ejb.serviceurl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ServiceURLHomeInterface extends EJBHome {
    public ServiceURLRemoteInterface create() throws RemoteException, CreateException;
}
