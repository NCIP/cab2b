package edu.wustl.cab2b.common.ejb.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;

import edu.wustl.cab2b.common.BusinessInterface;

public interface ServiceURLBusinessInterface extends BusinessInterface {
    Collection<String> getAllApplicationNames() throws RemoteException;
}
