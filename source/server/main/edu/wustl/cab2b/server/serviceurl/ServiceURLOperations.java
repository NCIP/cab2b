package edu.wustl.cab2b.server.serviceurl;

import static edu.wustl.cab2b.common.util.Constants.CATEGORY_ENTITY_GROUP_NAME;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

public class ServiceURLOperations {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ServiceURLOperations.class);

    public Collection<String> getAllApplicationNames() throws RemoteException {
        Collection<ServiceURL> serviceList = getAllServiceURLs();
        Collection<String> applicationNames = new HashSet<String>();
        for (ServiceURL serviceURL : serviceList) {
            applicationNames.add(serviceURL.getEntityGroupName());
        }
        //TODO temporarily adding constant here. LAter thsi should be a change in database
        applicationNames.add(CATEGORY_ENTITY_GROUP_NAME);

        return applicationNames;
    }

    public Collection<ServiceURL> getAllServiceURLs() throws RemoteException {
        Collection<ServiceURL> serviceURLs = null;

        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURL> dbHandler = new HibernateDatabaseOperations<ServiceURL>(
                session);

        serviceURLs = dbHandler.retrieve(ServiceURL.class.getName());
        // serviceURLs = (List<ServiceURL>) retrieve(ServiceURL.class.getName());

        return serviceURLs;
    }

    /**
     * 
     * @return List of all the services available in cab2b model
     * @throws RemoteException 
     * @throws RemoteException 
     * @throws MalformedURIException 
     */
    public List<AdminServiceMetadata> getInstancesByServiceName(String serviceName, UserInterface user)
            throws RemoteException {
        final IndexServiceOperations indexServiceOperations = new IndexServiceOperations();
        EndpointReferenceType[] allServices = null;
        try {
            allServices = indexServiceOperations.getServicesByNames(serviceName);
        } catch (MalformedURIException e) {
            // TODO Auto-generated catch block
            logger.info(e.getMessage(), e);
        } catch (RemoteException remoteException) {
            logger.info(remoteException.getMessage(), remoteException);
            throw new RemoteException(remoteException.getMessage());
        }
        return getMetadataObjects(allServices, serviceName, user);
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
                                                          String serviceName, UserInterface user) {
        final List<AdminServiceMetadata> metadataObjectService = new ArrayList<AdminServiceMetadata>();
        // final List<AdminServiceMetadata> tempMetadataObject = new ArrayList<AdminServiceMetadata>();
        Map<String, AdminServiceMetadata> existingServiceURLMap = getExistingServiceURLs(serviceName, user);

        AdminServiceMetadata metadataObject = null;

        for (EndpointReferenceType service : services) {

            AttributedURI attributeUri = service.getAddress();
            String serviceURL = attributeUri.toString();
            if (existingServiceURLMap.containsKey(serviceURL)) {
                metadataObject = existingServiceURLMap.get(serviceURL);
                existingServiceURLMap.remove(serviceURL);
            } else {
                metadataObject = new AdminServiceMetadata();
                ServiceURL serviceURLObj = new ServiceURL();
                serviceURLObj.setUrlLocation(serviceURL);
                serviceURLObj.setEntityGroupName(serviceName);
                serviceURLObj.setAdminDefined(user.isAdmin());
                metadataObject.setServiceURL(attributeUri.toString());
                metadataObject.setServiceURLObject(serviceURLObj);

            }
            ServiceMetadata metaData = null;
            try {
                metaData = MetadataUtils.getServiceMetadata(service);
            } catch (ResourcePropertyRetrievalException e) {
                logger.info(e.getMessage(), e);
            }
            String description = "* No description available";
            String researchCenter = metadataObject.getServiceURL();
            //null checks to avoid null pointers
            if(metaData!=null) {
                if(metaData.getServiceDescription()!=null && metaData.getServiceDescription().getService()!=null ){
                    String desc = metaData.getServiceDescription().getService().getDescription();
                    if (desc != null && !desc.equals("")) {
                        description = desc;
                    }
                }
                if(metaData.getHostingResearchCenter() !=null && metaData.getHostingResearchCenter().getResearchCenter()!=null) {
                    String name = metaData.getHostingResearchCenter().getResearchCenter().getDisplayName();
                    if (name != null && !name.equals("")) {
                        researchCenter = name;
                    }
                }
            } 
            metadataObject.setSeviceDescription(description);
            metadataObject.setHostingResearchCenter(researchCenter);
            metadataObject.setSeviceName(serviceName);
            metadataObjectService.add(metadataObject);
        }
        for (String serviceURL : existingServiceURLMap.keySet()) {
            AdminServiceMetadata metadata = existingServiceURLMap.get(serviceURL);

            metadataObjectService.add(metadata);
        }
        Collections.sort(metadataObjectService, new ServiceSorter());

        return metadataObjectService;
    }

    /**
     * This method returns a map of service url against servicemetadata.
     * It will also mark all the service urls as selected which are alredy configured for the user.     
     * @return
     */
    public Map<String, AdminServiceMetadata> getExistingServiceURLs(String serviceName, UserInterface user) {
        Map<String, AdminServiceMetadata> serviceURLMetadataMap = new HashMap<String, AdminServiceMetadata>();
        Collection<ServiceURLInterface> existingServiceURLList = new HashSet<ServiceURLInterface>();
        Map<String, List<String>> entityGroupVsURLS = new UserOperations().getServiceURLsForUser(user);
        List<String> serviceURLS = entityGroupVsURLS.get(serviceName);
        //   Collection<ServiceURLInterface> userServiceURLS = user.getServiceURLCollection();
        existingServiceURLList.addAll(getAllInstancesForEntityGroup(serviceName).values());
        for (ServiceURLInterface service : existingServiceURLList) {
            AdminServiceMetadata metadataObject = new AdminServiceMetadata();
            String serviceURL = service.getUrlLocation();
            metadataObject.setServiceURL(serviceURL);
            metadataObject.setSeviceDescription("* No description available");
            metadataObject.setHostingResearchCenter(serviceURL);
            metadataObject.setSeviceName(service.getEntityGroupName());
            metadataObject.setServiceURLObject(service);
            if (serviceURLS.contains(service.getUrlLocation())) {
                metadataObject.setConfigured(true);
            }
            serviceURLMetadataMap.put(serviceURL, metadataObject);
        }

        return serviceURLMetadataMap;
    }

    /**
     * This method will return a collection of all the service instances present in the database for the
     * entityGroupName.
     * @param entityGroupName
     * @return
     */
    public Map<String, ServiceURL> getAllInstancesForEntityGroup(String entityGroupName) {
        Map<String, ServiceURL> serviceURLMap = new HashMap<String, ServiceURL>();
        Collection<ServiceURL> serviceURLList;

        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURL> dbHandler = new HibernateDatabaseOperations<ServiceURL>(session);

        serviceURLList = dbHandler.retrieve(ServiceURL.class.getName(), "entityGroupName", entityGroupName);

        for (ServiceURL serviceURL : serviceURLList) {
            serviceURLMap.put(serviceURL.getUrlLocation(), serviceURL);
        }

        return serviceURLMap;
    }

}

class ServiceSorter implements Comparator<AdminServiceMetadata> {

    public int compare(AdminServiceMetadata obj1, AdminServiceMetadata obj2) {

        if (obj1.isConfigured() && obj2.isConfigured()) {
            return obj1.getServiceURL().compareTo(obj2.getServiceURL());
        } else if (obj1.isConfigured() && !obj2.isConfigured()) {
            return -1;
        } else if (!obj1.isConfigured() && obj2.isConfigured()) {
            return 1;
        }

        return 0;
    }
}