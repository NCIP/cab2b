package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.axis.types.URI.MalformedURIException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author Hrishikesh Rajpathak
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
    public List<ServiceURL> getServiceMetadataObjects(String serviceName, String version,
                                                                edu.wustl.cab2b.common.user.UserInterface user)
            throws MalformedURIException, RemoteException {

        return new ServiceURLOperations().getInstancesByServiceName(serviceName, version, user);

    }

    /**
     * This method fetches all the entity groups created as part of metadata
     * creation and returns them.
     * 
     * TODO:For efficiency, we can chop off entity collection from entity group
     * object. It is not incorporated currently.
     * 
     * @return All the metadata entity groups
     */
    public Collection<EntityGroupInterface> getMetadataEntityGroups() {
        Collection<EntityGroupInterface> entityGroups = EntityCache.getInstance().getEntityGroups();

        Collection<EntityGroupInterface> filteredGroups = new ArrayList<EntityGroupInterface>();
        for (EntityGroupInterface entityGroup : entityGroups) {
            if (Constants.CATEGORY_ENTITY_GROUP_NAME.equalsIgnoreCase(entityGroup.getLongName())
                    || Constants.DATALIST_ENTITY_GROUP_NAME.equalsIgnoreCase(entityGroup.getLongName())) {
                continue;
            }
            filteredGroups.add(entityGroup);
        }

        return filteredGroups;
    }

    /**
     * This method saves the admin selected services to the repository.
     * @param entityGroupName
     * @param serviceMetadataObjects
     * @param currentUser
     * @return
     * @throws RemoteException
     */
    public User saveServiceInstances(String entityGroupName,
                                     Collection<ServiceURL> serviceMetadataObjects, User currentUser)
            throws RemoteException {

        Collection<ServiceURL> serviceURLObjects = new HashSet<ServiceURL>();

        for (ServiceURL serviceMetadata : serviceMetadataObjects) {
            serviceURLObjects.add(serviceMetadata);
        }

        if (serviceURLObjects.isEmpty()) {
            Collection<ServiceURLInterface> serviceURLsToRemove = new HashSet<ServiceURLInterface>();
            Collection<ServiceURLInterface> allServiceURLLIst = currentUser.getServiceURLCollection();
            for (ServiceURLInterface serviceURL : allServiceURLLIst) {
                String serviceName = serviceURL.getEntityGroupName();
                if (serviceName.equalsIgnoreCase(entityGroupName)) {
                    serviceURLsToRemove.add(serviceURL);
                }
            }
            currentUser.getServiceURLCollection().removeAll(serviceURLsToRemove);
            return saveUser(currentUser);

        }

        Collection<ServiceURLInterface> newServices = new HashSet<ServiceURLInterface>();
        newServices.addAll(serviceURLObjects);

        Collection<ServiceURLInterface> allServiceURLLIst = currentUser.getServiceURLCollection();
        Collection<ServiceURLInterface> serviceURLsToRemove = new HashSet<ServiceURLInterface>();
        for (ServiceURLInterface serviceURL : allServiceURLLIst) {
            String serviceName = serviceURL.getEntityGroupName();
            if (serviceName.equalsIgnoreCase(entityGroupName)) {
                if (newServices.contains(serviceURL)) {
                    newServices.remove(serviceURL);
                } else {
                    serviceURLsToRemove.add(serviceURL);
                }
            }

        }

        currentUser.getServiceURLCollection().removeAll(serviceURLsToRemove);
        currentUser.getServiceURLCollection().addAll(newServices);
        return saveUser(currentUser);
    }

    private User saveUser(User user) throws RemoteException {
        if (user.getUserId() != null) {
            new UserOperations().updateUser(user);

        }
        return user;
    }

}
