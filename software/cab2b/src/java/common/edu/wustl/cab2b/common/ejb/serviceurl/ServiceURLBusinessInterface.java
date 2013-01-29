/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.ejb.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;

import edu.wustl.cab2b.common.BusinessInterface;
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
    Collection<ServiceURLInterface> getInstancesByServiceName(String serviceName, String version, UserInterface user)
            throws RemoteException;
    
    /**
     * This method saves the collection of user defined ServiceURL for a particular entityGroupName and for that user
     * @param entityGroupName
     * @param serviceMetadataObjects
     * @param currentUser
     * @return
     * @throws RemoteException
     */
    UserInterface saveServiceInstances(String entityGroupName, Collection<ServiceURLInterface> serviceMetadataObjects,
                              UserInterface currentUser) throws RemoteException ;

}
