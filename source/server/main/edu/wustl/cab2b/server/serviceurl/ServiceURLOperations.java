package edu.wustl.cab2b.server.serviceurl;

import static edu.wustl.cab2b.common.util.Constants.CATEGORY_ENTITY_GROUP_NAME;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;

/**
 * @author gaurav_mehta
 * This class performs operations related to the Service URL functionality
 */
public class ServiceURLOperations {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ServiceURLOperations.class);

    /**
     * @return Names of all the Entity Group present in the database
     * @throws RemoteException
     */
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

    /**
     * @return all the service URL present in the database 
     * @throws RemoteException
     */
    public Collection<ServiceURL> getAllServiceURLs() throws RemoteException {
        Collection<ServiceURL> serviceURLs = null;

        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURL> dbHandler = new HibernateDatabaseOperations<ServiceURL>(session);

        serviceURLs = dbHandler.retrieve(ServiceURL.class.getName());
        return serviceURLs;
    }

    /**
     * @param serviceName
     * @param version
     * @param user
     * @return List of service URLs for a particular Entity Group from database and Index Service
     * @throws RemoteException
     */
    public List<ServiceURL> getInstancesByServiceName(String serviceName, String version, UserInterface user)
            throws RemoteException {
        final IndexServiceOperations indexServiceOperations = new IndexServiceOperations();
        Map<String, ServiceMetadata> allServices = null;
        try {
            allServices = indexServiceOperations.getServicesByNames(serviceName, version);
        } catch (MalformedURIException e) {
            logger.info(e.getMessage(), e);
        } catch (RemoteException remoteException) {
            logger.info(remoteException.getMessage(), remoteException);
            throw new RemoteException(remoteException.getMessage());
        }
        return getMetadataObjects(allServices, serviceName, version, user);
    }

    /**
     * This function gets Service URLs from database and Index Service and gives them to merge Function
     * @param services
     * @param serviceName
     * @param version
     * @param user
     * @return List of Service URLs
     */
    private List<ServiceURL> getMetadataObjects(final Map<String, ServiceMetadata> services, String serviceName,
                                                String version, UserInterface user) {

        String entityGroupName = Utility.createModelName(serviceName, version);

        Map<String, ServiceURL> urlsFromDatabase = getExistingServiceURLs(entityGroupName, user);
        Map<String, ServiceURL> urlsFromIndexService = generateServiceUrls(entityGroupName, services);

        return merge(urlsFromDatabase, urlsFromIndexService);
    }


    /**
     * It performs a two way merging.
     * If Service URL from Index Service contains Hosting Center Name and Description then Those are associated 
     * with that Service URL and returned. So changes are made in Database object and returned instead of Index Service
     * object as Database object has UrlId associated with it saved in database which is not the case with Index Service
     * URL. Those URLs which are not present in database are returned as it is.   
     * @param urlsFromDatabase
     * @param urlsFromIndexService
     * @return mergerd list of Service URLs
     */
    List<ServiceURL> merge(Map<String, ServiceURL> urlsFromDatabase, Map<String, ServiceURL> urlsFromIndexService) {

        List<ServiceURL> listofServiceURL = new ArrayList<ServiceURL>();

        for (String serviceURL : urlsFromIndexService.keySet()) {
            ServiceURL urlMetadatFromIndexService = urlsFromIndexService.get(serviceURL);
            if (urlsFromDatabase.containsKey(serviceURL)) {
                ServiceURL urlMetadataFromDatabase = urlsFromDatabase.get(serviceURL);
                String description = urlMetadatFromIndexService.getDescription();
                if (!description.isEmpty() && description.length()>0) {
                    urlMetadataFromDatabase.setDescription(description);
                }
                String hostingCenterName = urlMetadatFromIndexService.getHostingCenterName();
                if (!hostingCenterName.isEmpty() && hostingCenterName.length()>0) {
                    urlMetadataFromDatabase.setHostingCenterName(hostingCenterName);
                }
                listofServiceURL.add(urlMetadataFromDatabase);
            } else {
                listofServiceURL.add(urlMetadatFromIndexService);
            }
        }
        return listofServiceURL;
    }

    /**
     * converts the ServiceMetadata object received from IndexService into ServiceURL object 
     * and returns a Map of UrlVsMetadata
     * @param entityGroupName
     * @param services
     * @return
     */
    private Map<String, ServiceURL> generateServiceUrls(String entityGroupName,
                                                        Map<String, ServiceMetadata> services) {
        Map<String, ServiceURL> urlVsMetadata = new HashMap<String, ServiceURL>(services.size());
        for (String url : services.keySet()) {
            ServiceMetadata metadataFromIndex = services.get(url);

            String description = "";
            String hostingCenterName = "";
            if (metadataFromIndex != null) {
                if (metadataFromIndex.getServiceDescription() != null
                        && metadataFromIndex.getServiceDescription().getService() != null) {
                    description = metadataFromIndex.getServiceDescription().getService().getDescription();
                }

                if (metadataFromIndex.getHostingResearchCenter() != null
                        && metadataFromIndex.getHostingResearchCenter().getResearchCenter() != null) {
                    hostingCenterName = metadataFromIndex.getHostingResearchCenter().getResearchCenter().getDisplayName();
                }
            }

            ServiceURL serviceURL = new ServiceURL();
            serviceURL.setDescription(description);
            serviceURL.setHostingCenterName(hostingCenterName);
            serviceURL.setUrlLocation(url);
            serviceURL.setEntityGroupName(entityGroupName);
            serviceURL.setAdminDefined(false);
            serviceURL.setConfigured(false);

            urlVsMetadata.put(url, serviceURL);
        }
        return urlVsMetadata;
    }

    /**
     * This method returns a map of service urlVsMetadata from database.
     * It will also mark all the service urls as selected which are already configured for the user.     
     * @return
     */
    private Map<String, ServiceURL> getExistingServiceURLs(String entityName, UserInterface user) {

        Map<String, ServiceURL> serviceURLMetadataMap = new HashMap<String, ServiceURL>();
        Collection<ServiceURL> existingServiceURLList = new HashSet<ServiceURL>();

        Map<String, List<String>> entityGroupVsURLS = new UserOperations().getServiceURLsForUser(user);
        List<String> serviceURLS = entityGroupVsURLS.get(entityName);

        existingServiceURLList.addAll(getAllInstancesForEntityGroup(entityName));

        for (ServiceURL service : existingServiceURLList) {
            if (serviceURLS != null && serviceURLS.contains(service.getUrlLocation())) {
                service.setConfigured(true);
            }
            serviceURLMetadataMap.put(service.getUrlLocation(), service);
        }
        return serviceURLMetadataMap;
    }

    /**
     * This method will return a collection of all the service instances present in the database for the
     * entityGroupName.
     * @param entityGroupName
     * @return
     */
    public List<ServiceURL> getAllInstancesForEntityGroup(String entityGroupName) {
        List<ServiceURL> serviceURLMap = new ArrayList<ServiceURL>();

        Session session = HibernateUtil.newSession();
        try {
            HibernateDatabaseOperations<ServiceURL> dbHandler = new HibernateDatabaseOperations<ServiceURL>(
                    session);
            Collection<ServiceURL> serviceURLList = dbHandler.retrieve(ServiceURL.class.getName(),
                                                                       "entityGroupName", entityGroupName);
            for (ServiceURL serviceURL : serviceURLList) {
                serviceURLMap.add(serviceURL);
            }
            return serviceURLMap;
        } finally {
            session.close();
        }
    }

}

class ServiceSorter implements Comparator<ServiceURL> {

    public int compare(ServiceURL obj1, ServiceURL obj2) {

        if (obj1.isConfigured() && obj2.isConfigured()) {
            return obj1.getUrlLocation().compareTo(obj2.getUrlLocation());
        } else if (obj1.isConfigured() && !obj2.isConfigured()) {
            return -1;
        } else if (!obj1.isConfigured() && obj2.isConfigured()) {
            return 1;
        }

        return 0;
    }
}