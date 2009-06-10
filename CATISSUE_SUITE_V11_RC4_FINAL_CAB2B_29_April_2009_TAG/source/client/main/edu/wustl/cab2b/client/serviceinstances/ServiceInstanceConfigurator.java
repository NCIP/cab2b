package edu.wustl.cab2b.client.serviceinstances;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.cache.UserCache;
import edu.wustl.cab2b.client.ui.mainframe.UserValidator;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLBusinessInterface;
import edu.wustl.cab2b.common.ejb.serviceurl.ServiceURLHomeInterface;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.ejb.user.UserHomeInterface;
import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Constants;

/**
 * @author Hrishikesh Rajpathak
 * @author atul_jawale
 */
public class ServiceInstanceConfigurator {

    private static final Logger logger = Logger.getLogger(ServiceInstanceConfigurator.class);

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
        Collection<EntityGroupInterface> entityGroups = ClientSideCache.getInstance().getEntityGroups();

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
     * This method saves the service instance into the database. 
     * It gets all the service instance selected by user and add to user serviceURL collection
     * and updates the user. 
     * @param serviceURLObjects
     * @param user
     * @throws RemoteException
     */
    public void saveServiceInstances(String entityGroupName, Collection<ServiceURL> serviceURLObjects)
            throws RemoteException {
        /*
         * If the user has clicked on admin configured urls,then collection will be empty
         * so just removing all the service urls from user's service url collection whose
         * service name is same as entityGroupName
         */
        UserInterface currentUser = getCurrentUser();

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
            saveUser(currentUser);
            UserCache.getInstance().init(currentUser);
            return;
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
        UserCache.getInstance().init(currentUser);
        saveUser(currentUser);
        return;
    }

    /**
     * This method gets all the service instances for service name.
     * This includes a union of all the instances configured already to the repository with all the
     * running instances.  
     * @param serviceName
     * @param user
     * @return
     */
    public Collection<AdminServiceMetadata> getServiceMetadataObjects(String serviceName, UserInterface user) {
        ServiceURLBusinessInterface serviceURLInterface = (ServiceURLBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                         EjbNamesConstants.SERVICE_URL_BEAN,
                                                                                                                         ServiceURLHomeInterface.class,
                                                                                                                         null);
        Collection<AdminServiceMetadata> serviceInstanceList = new ArrayList<AdminServiceMetadata>();

        try {
            serviceInstanceList.addAll(serviceURLInterface.getInstancesByServiceName(serviceName, user));
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
                                                                                                           UserHomeInterface.class,
                                                                                                           null);
            userInterface.updateUser(user);
        }
    }

    /**
     * This method updates the user- service url mapping 
     * @param user
     * @throws RemoteException
     */
    private UserInterface getCurrentUser() throws RemoteException {
        UserBusinessInterface userInterface = (UserBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                       EjbNamesConstants.USER_BEAN,
                                                                                                       UserHomeInterface.class,
                                                                                                       null);
        return userInterface.getUserByName(UserValidator.getSerializedDelegatedCredReference());
    }

}
