package edu.wustl.cab2b.server.ejb.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLBusinessInterface;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;

/**
 * 
 * @author lalit_chand, atul_jawale
 *
 */
public class ServiceURLBean extends AbstractStatelessSessionBean implements ServiceURLBusinessInterface {


    private static final long serialVersionUID = 1L;

	/**
	 * This method returns a collection all the Application names present in the database
	 * @return
	 * @throws RemoteException
	 */
    public Collection<String> getAllApplicationNames() throws RemoteException {
        return new ServiceURLOperations().getAllApplicationNames();
    }

    /**
     * This method returns a collection all the service instances present in the database
     * @return
     * @throws RemoteException
     */
    public Collection<ServiceURL> getAllServiceURLs() throws RemoteException {
        return new ServiceURLOperations().getAllServiceURLs();
    }

    /**
     * This method returns a collection of all the AdminServiceMetadata which is a union of 
     * all the service instances present in the database and the running instances.  
     * 
     * @param serviceName
     * @param user
     * @return
     * @throws RemoteException
     */
    public Collection<AdminServiceMetadata> getInstancesByServiceName(String serviceName, UserInterface user)
            throws RemoteException {
        return new ServiceURLOperations().getInstancesByServiceName(serviceName, user);
    }

    /**
     * This method returns a map of service url string against the serviceURL object of all the serviceURLs present in the database only
     * @param serviceName
     * @return
     * @throws RemoteException
     */
    public Map<String,? extends ServiceURLInterface> getAllInstancesForEntityGroup(String serviceName)
            throws RemoteException {
        // TODO Auto-generated method stub
        return new ServiceURLOperations().getAllInstancesForEntityGroup(serviceName);
    }
}
