package edu.wustl.cab2b.server.ejb.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;

import edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLBusinessInterface;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;

/**
 * 
 * @author lalit_chand, atul_jawale , 
 * @author Gaurav Mehta
 *
 */
public class ServiceURLBean extends AbstractStatelessSessionBean implements ServiceURLBusinessInterface {

    private static final long serialVersionUID = 1L;

    /**
     * This method returns a collection all the Application names present in the database
     * @return Collection of all application names
     * @throws RemoteException
     */
    public Collection<String> getAllApplicationNames() throws RemoteException {
        return new ServiceURLOperations().getAllApplicationNames();
    }

    /**
     * This method returns a collection all the service instances present in the database
     * @return Collection of all service urls.
     * @throws RemoteException
     */
    public Collection<ServiceURLInterface> getAllServiceURLs() throws RemoteException {
        return new ServiceURLOperations().getAllServiceURLs();
    }

    /**
     * This method returns a collection of all the ServiceURL which is a union of 
     * all the service instances present in the database and the running instances.  
     * 
     * @param serviceName
     * @param user
     * @return Instances by service names
     * @throws RemoteException
     */
    public Collection<ServiceURLInterface> getInstancesByServiceName(String serviceName, String version,
                                                                     UserInterface user) throws RemoteException {
        return new ServiceURLOperations().getInstancesByServiceName(serviceName, version, user);
    }

    /**
     * This method saves a collection of ServiceURL of a particular entityGroup for given user
     * @param entityGroupName
     * @param serviceMetadataObjects
     * @param currentUser
     * @return user object with serviceURLS configured
     * @throws RemoteException
     * @see edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLBusinessInterface#saveServiceInstances(java.lang.String, java.util.Collection, edu.wustl.cab2b.common.user.UserInterface)
     */
    public UserInterface saveServiceInstances(String entityGroupName,
                                              Collection<ServiceURLInterface> serviceMetadataObjects,
                                              UserInterface currentUser) throws RemoteException {
        return new ServiceURLOperations().saveServiceInstances(entityGroupName, serviceMetadataObjects,
                                                               currentUser);
    }
}
