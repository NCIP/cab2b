/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.serviceinstances;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.client.cache.UserCache;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLBusinessInterface;
import edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLHomeInterface;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.ejb.user.UserHomeInterface;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;

/**
 * @author Hrishikesh Rajpathak
 * @author atul_jawale
 * @author Gaurav Mehta
 */
public class ServiceInstanceConfigurator {

    private static final Logger logger = Logger.getLogger(ServiceInstanceConfigurator.class);

    /**
      * This method saves the service instance into the database. 
      * It gets all the service instance selected by user and add to user serviceURL collection
      * and updates the user. 
      * @param serviceURLObjects
      * @param entityName
      * @throws RemoteException
      */
    public void saveServiceInstances(String entityGroupName, Collection<ServiceURLInterface> serviceURLObjects,
                                     UserInterface currentUser) throws RemoteException {
        ServiceURLBusinessInterface serviceURLInterface = (ServiceURLBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                         EjbNamesConstants.SERVICE_URL_BEAN,
                                                                                                                         ServiceURLHomeInterface.class);
        currentUser = serviceURLInterface.saveServiceInstances(entityGroupName, serviceURLObjects, currentUser);
        saveUser(currentUser);
        UserCache.getInstance().init(currentUser);
    }

    /**
     * This method gets all the service instances for service name.
     * This includes a union of all the instances configured already to the repository with all the
     * running instances.  
     * @param serviceName
     * @param user
     * @return
     */
    public Collection<ServiceURLInterface> getServiceMetadataObjects(String serviceName, String version, UserInterface user) {
        ServiceURLBusinessInterface serviceURLInterface = (ServiceURLBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                         EjbNamesConstants.SERVICE_URL_BEAN,
                                                                                                                         ServiceURLHomeInterface.class);
        Collection<ServiceURLInterface> serviceInstanceList = new ArrayList<ServiceURLInterface>();
        try {
            serviceInstanceList.addAll(serviceURLInterface.getInstancesByServiceName(serviceName, version, user));
        } catch (RemoteException e) {
            logger.error(e.getStackTrace(), e);
            throw new RuntimeException(e.getMessage());
        }
        return serviceInstanceList;
    }

    /**
     * This method updates the user- service url mapping 
     * @param user
     * @throws RemoteException
     */
    private void saveUser(UserInterface user) throws RemoteException {
        if (user.getUserId() != null) {
            UserBusinessInterface userInterface = (UserBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                           EjbNamesConstants.USER_BEAN,
                                                                                                           UserHomeInterface.class);
            userInterface.updateUser(user);
        }
    }

}
