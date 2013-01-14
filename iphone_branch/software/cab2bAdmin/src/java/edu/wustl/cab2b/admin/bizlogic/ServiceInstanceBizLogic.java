/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import org.apache.axis.types.URI.MalformedURIException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.ServiceURLUtility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author Hrishikesh Rajpathak , Gaurav Mehta
 *
 */
public class ServiceInstanceBizLogic {
    /**
     *
     * @param serviceName
     * @param user
     * @param version
     * @return List of all the services avaliable in cab2b model
     * @throws RemoteException
     * @throws MalformedURIException
     */
    public List<ServiceURLInterface> getServiceMetadataObjects(String serviceName, String version,
                                                               edu.wustl.cab2b.common.user.UserInterface user)
            throws MalformedURIException, RemoteException {
        return new ServiceURLOperations().getInstancesByServiceName(serviceName, version, user);
    }

    /**
     * This method fetches all the entity groups created as part of metadata
     * creation and returns them.
     * TODO:For efficiency, we can chop off entity collection from entity group
     * object. It is not incorporated currently.
     * @return All the metadata entity groups
     */
    public Collection<EntityGroupInterface> getMetadataEntityGroups() {
        return new ServiceURLUtility().getMetadataEntityGroups(EntityCache.getInstance());
    }

    /**
     * This method saves the admin selected services to the repository.
     * @param entityGroupName
     * @param serviceMetadataObjects
     * @param currentUser
     * @return
     * @throws RemoteException
     */
    public UserInterface saveServiceInstances(String entityGroupName,
                                              Collection<ServiceURLInterface> serviceMetadataObjects,
                                              UserInterface currentUser) throws RemoteException {
        currentUser = new ServiceURLOperations().saveServiceInstances(entityGroupName, serviceMetadataObjects,
                                                                      currentUser);
        return saveUser(currentUser);
    }

    private UserInterface saveUser(UserInterface user) throws RemoteException {
        if (user.getUserId() != null) {
            new UserOperations().updateUser(user);
        }
        return user;
    }

}
