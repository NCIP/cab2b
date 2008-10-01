package edu.wustl.cab2b.common.ejb.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;

public interface ServiceURLBusinessInterface extends BusinessInterface {
    Collection<String> getAllApplicationNames() throws RemoteException;

    Collection<? extends ServiceURLInterface> getAllServiceURLs() throws RemoteException;

    Collection<AdminServiceMetadata> getInstancesByServiceName(String serviceName, UserInterface user)
            throws RemoteException;

    Map<String,? extends ServiceURLInterface> getAllInstancesForEntityGroup(String serviceName) throws RemoteException;
    
}
