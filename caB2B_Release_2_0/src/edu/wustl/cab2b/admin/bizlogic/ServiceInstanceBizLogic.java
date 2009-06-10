package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.admin.beans.AdminServiceMetadata;
import edu.wustl.cab2b.admin.services.IndexServiceOperations;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

/**
 * @author Hrishikesh Rajpathak
 * 
 */
public class ServiceInstanceBizLogic {

    /**
     * 
     * @return List of all the services avaliable in cab2b model
     * @throws RemoteException 
     * @throws MalformedURIException 
     */
    public List<AdminServiceMetadata> getServiceMetadataObjects(String serviceName) throws MalformedURIException,
            RemoteException {
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
     * @throws RemoteException 
     */
    private List<AdminServiceMetadata> getMetadataObjects(final EndpointReferenceType[] services,
                                                          String serviceName) {
        final List<AdminServiceMetadata> metadataObjectService = new ArrayList<AdminServiceMetadata>();

        for (EndpointReferenceType service : services) {
            AdminServiceMetadata metadataObject = new AdminServiceMetadata();
            AttributedURI attributeUri = service.getAddress();
            metadataObject.setServiceURL(attributeUri);
            String description = null;
            ServiceMetadata metaData = null;
            try {
                metaData = MetadataUtils.getServiceMetadata(service);
            } catch (InvalidResourcePropertyException e) {
                Logger.out.error(e.getMessage(), e);
                // throw new RemoteException(e.getMessage());
            } catch (RemoteResourcePropertyRetrievalException e) {
                Logger.out.error(e.getMessage(), e);
                //  throw new RemoteException(e.getMessage());
            } catch (ResourcePropertyRetrievalException e) {
                Logger.out.error(e.getMessage(), e);
                //  throw new RemoteException(e.getMessage());
            }

            try {
                description = metaData.getServiceDescription().getService().getDescription();
                if (description == null || description.equals("")) {
                    description = "* No description available";
                }
                metadataObject.setSeviceDescription(description);
                metadataObject.setHostingResearchCenter(metaData.getHostingResearchCenter().getResearchCenter().getDisplayName());
            } catch (NullPointerException e) {
                //TODO If metadata object is returned null, how to set the description and hosting research info
                Logger.out.error(e.getMessage(), e);
                metadataObject.setHostingResearchCenter(metadataObject.getServiceURL().toString());
                if (description == null || description.equals("")) {
                    description = "* No description available";
                }
                metadataObject.setSeviceDescription(description);
            }
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

    public void saveServiceInstances(Collection<AdminServiceMetadata> serviceMetadataObjects, User user)
            throws RemoteException {
        for (AdminServiceMetadata serviceMetadata : serviceMetadataObjects) {
            String entityGroupName = serviceMetadata.getSeviceName();
            try {
                EntityGroupInterface groupInterface = EntityManager.getInstance().getEntityGroupByName(
                                                                                                       entityGroupName);
                entityGroupName = groupInterface.getLongName();
            } catch (DynamicExtensionsSystemException e) {
                Logger.out.error(e.getMessage(), e);
                throw new RemoteException(e.getMessage());
            } catch (DynamicExtensionsApplicationException e) {
                Logger.out.error(e.getMessage(), e);
                throw new RemoteException(e.getMessage());
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

}
