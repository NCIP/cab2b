package edu.wustl.cab2b.common.ejb.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * @author gaurav_mehta
 *
 */
public interface ServiceURLBusinessInterface extends BusinessInterface {

    /**
     * This method returns a collection all the Application names present in the database
     * @return
     * @throws RemoteException
     */
    Collection<String> getAllApplicationNames() throws RemoteException;

    /**
     * This method returns a collection all the service instances present in the database
     * @return
     * @throws RemoteException
     */
    Collection<? extends ServiceURLInterface> getAllServiceURLs() throws RemoteException;

    /**
     * This method returns a collection of all the ServiceURL which is a union of 
     * all the service instances present in the database and the running instances.  
     * 
     * @param serviceName
     * @param user
     * @return
     * @throws RemoteException
     */
    Collection<ServiceURL> getInstancesByServiceName(String serviceName, String version, UserInterface user)
            throws RemoteException;

}
