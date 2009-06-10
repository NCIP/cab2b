package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.admin.beans.AdminServiceMetadata;
import edu.wustl.cab2b.admin.services.IndexServiceOperations;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author Hrishikesh Rajpathak
 * 
 */
public class ServiceInstanceBizLogic {

    /**
     * 
     * @return List of all the services avaliable in cab2b model
     */
    public List<AdminServiceMetadata> getServiceMetadataObjects(String serviceName) throws Exception {
        final IndexServiceOperations indexServiceOperations = new IndexServiceOperations();
        final EndpointReferenceType[] allServices = indexServiceOperations.getServicesByNames(serviceName);
        return getMetadataObjects(allServices, serviceName);
    }

    /**
     * returns metadata objects for each service.
     * 
     * @param array
     *            of type EndpointReferenceType
     * @return list of AdminServiceMetadata
     */
    private List<AdminServiceMetadata> getMetadataObjects(final EndpointReferenceType[] services,
                                                          String serviceName) {
        final List<AdminServiceMetadata> metadataObjectService = new ArrayList<AdminServiceMetadata>();

        for (EndpointReferenceType service : services) {
            AdminServiceMetadata metadataObject = new AdminServiceMetadata();
            AttributedURI attributeUri = service.getAddress();
            metadataObject.setServiceURL(attributeUri);

            String path = attributeUri.getPath();
            int pos = path.lastIndexOf("/");
            String name = path.substring(pos + 1, path.length());
            metadataObject.setHostingResearchCenter(attributeUri.getHost());
            metadataObject.setSeviceDescription("Service is : " + name + " This is dummy service description");
            metadataObject.setSeviceName(serviceName);
            metadataObjectService.add(metadataObject);
        }
        return metadataObjectService;
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
        return EntityCache.getInstance().getEntityGroups();
    }

    public void saveServiceInstances(Collection<AdminServiceMetadata> serviceMetadataObjects, String userName)
            throws RemoteException {
        User user = getUserByName(userName);

        for (AdminServiceMetadata serviceMetadata : serviceMetadataObjects) {
            String entityGroupName = serviceMetadata.getSeviceName();
            try {
                EntityGroupInterface groupInterface = EntityManager.getInstance().getEntityGroupByName(
                                                                                                       entityGroupName);
                entityGroupName = groupInterface.getLongName();
            } catch (DynamicExtensionsSystemException e) {
                e.printStackTrace();
            } catch (DynamicExtensionsApplicationException e) {
                e.printStackTrace();
            }

            ServiceURL serviceURL = new ServiceURL();
            serviceURL.setEntityGroupName(entityGroupName);
            serviceURL.setUrlLocation(serviceMetadata.getServiceURL().toString());
            serviceURL.addUser(user);
            serviceURL.setAdminDefined(true);

            user.addServiceURL(serviceURL);
        }
        saveUser(user);
    }

    private void saveUser(User user) throws RemoteException {
        if (user.getUserId() != null) {
            new UserOperations().updateUser(user);
        }
    }

    private User getUserByName(String userName) throws RemoteException {
        User user = null;
        if (userName != null) {
            user = new UserOperations().getUserByName("Admin");
        }
        return user;
    }
}
