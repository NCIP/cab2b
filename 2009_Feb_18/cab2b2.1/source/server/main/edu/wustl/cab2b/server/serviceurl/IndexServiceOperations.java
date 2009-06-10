package edu.wustl.cab2b.server.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.util.CommonPropertyLoader;
import edu.wustl.cab2b.common.util.ServiceURLUtility;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

/**
 * Handles all index service operations
 * @author gaurav_mehta
 */
public class IndexServiceOperations {

    private final static Logger logger = Logger.getLogger(IndexServiceOperations.class);

    /** It allows only one time entry into the thread for refreshing ServiceURL database */
    private static boolean entryFlag = true;

    /**
     * Private Constructor of the class
     */
    private IndexServiceOperations() {
    }

    /** As of now this method is invoked only by the context listeners of caB2B-Admin ,caB2BLite 
     * and first EJB call of caB2B. This will be changed once all three are merged into one common war 
     *  It starts the thread for refreshing teh ServiceURL database.
     */
    public static synchronized void refreshDatabase() {
        if (entryFlag) {
            entryFlag = false;
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    logger.info("Refreshing Database Started..........");
                    Collection<EntityGroupInterface> entityCollection = new ServiceURLUtility().getMetadataEntityGroups(EntityCache.getInstance());
                    for (EntityGroupInterface entityGroup : entityCollection) {
                        refreshServiceURLsMetadata(entityGroup.getLongName(), entityGroup.getVersion());
                    }
                    logger.info("Database refreshed.");
                    logger.info("Database in Sync with Index Service");
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 3600000 * 12);
        }
    }

    /** This method gets all the ServiceURLs from database for that EntityGroup and also from IndexService.
     * It adds them in a single Set so that URLs are not repeated as set doesn't allow storage of repeated entry
     * @param domainModel
     * @param version
     */
    static void refreshServiceURLsMetadata(String domainModel, String version) {
        String entityGroupName = Utility.createModelName(domainModel, version);

        ServiceURLOperations serviceUrlOperation = new ServiceURLOperations();
        Set<ServiceURL> urlsfromIndexService = new HashSet<ServiceURL>();
        Set<ServiceURL> refreshedURLMetadata = new HashSet<ServiceURL>();

        Set<ServiceURL> urlsfromDatabase = new HashSet<ServiceURL>(
                serviceUrlOperation.getAllURLsForEntityGroup(entityGroupName));
        try {
            urlsfromIndexService = getServiceURLsfromIndex(domainModel, version);
        } catch (MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        urlsfromDatabase.addAll(urlsfromIndexService);
        refreshedURLMetadata = refreshMetadata(urlsfromDatabase, entityGroupName);
        for (ServiceURL serviceURL : refreshedURLMetadata) {
            serviceUrlOperation.saveServiceURL(serviceURL);
        }
    }

    /**
     * This method returns the ServiceURLs from Index Service for given Service and its Version
     * @param domainModel
     * @param version
     * @return
     * @throws MalformedURIException
     * @throws RemoteException
     */
    static Set<ServiceURL> getServiceURLsfromIndex(String domainModel, String version)
            throws MalformedURIException, RemoteException {

        String urls = CommonPropertyLoader.getIndexServiceUrls();
        Set<ServiceURL> setofServiceURLs = new HashSet<ServiceURL>();
        DiscoveryClientForMetaData client = new DiscoveryClientForMetaData(urls);
        EndpointReferenceType[] endPointRefTypeArray;

        try {
            endPointRefTypeArray = client.discoverDataServicesByDomainModel(domainModel, version);
            if (endPointRefTypeArray != null) {
                for (EndpointReferenceType endPoint : endPointRefTypeArray) {
                    ServiceURL serviceurl = new ServiceURL();
                    serviceurl.setUrlLocation((endPoint.getAddress().toString()));
                    setofServiceURLs.add(serviceurl);
                }
            }
        } catch (RemoteResourcePropertyRetrievalException e) {
            e.printStackTrace();
        } catch (QueryInvalidException e) {
            e.printStackTrace();
        } catch (ResourcePropertyRetrievalException e) {
            e.printStackTrace();
        }
        return setofServiceURLs;
    }

    /**
     * This method gets the latest metadata for the URLs from Index Service as well as from Database
     * for given Domain Model and Version and updates its metadata accordingly
     * @param urlsfromDatabase
     * @param entityGroupName
     * @return
     */
    static Set<ServiceURL> refreshMetadata(Set<ServiceURL> urlsfromDatabase, String entityGroupName) {
        Set<ServiceURL> refreshedMetadata = new HashSet<ServiceURL>();
        for (ServiceURL urlMetatadatafromDatabase : urlsfromDatabase) {
            String urlLocation = urlMetatadatafromDatabase.getUrlLocation();
            ServiceURL urlMetadatafromIndexService = new ServiceURL();
            try {
                urlMetadatafromIndexService = getMetadata(urlLocation, entityGroupName);
            } catch (MalformedURIException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            refreshedMetadata.add(merge(urlMetatadatafromDatabase, urlMetadatafromIndexService));
        }
        return refreshedMetadata;
    }

    /**
     * This method gets the metadata from Index Service for a particular URL and converts it into 
     * ServiceURL object from ServiceMetadata object
     * @param serviceUrl
     * @param entityGroupName
     * @return
     * @throws MalformedURIException
     * @throws RemoteException
     */
    static ServiceURL getMetadata(String serviceUrl, String entityGroupName) throws MalformedURIException,
            RemoteException {
        String urls = CommonPropertyLoader.getIndexServiceUrls();
        DiscoveryClientForMetaData client = new DiscoveryClientForMetaData(urls);
        ServiceMetadata metadata = new ServiceMetadata();
        try {
            metadata = client.discoverDataServiceByURL(serviceUrl);
        } catch (ResourcePropertyRetrievalException e) {
            logger.error(e.getMessage(), e);
            throw new RemoteException(e.getMessage());
        }

        String description = "";
        String hostingCenterName = "";
        if (metadata != null) {
            if (metadata.getServiceDescription() != null && metadata.getServiceDescription().getService() != null) {
                description = metadata.getServiceDescription().getService().getDescription();
            }

            if (metadata.getHostingResearchCenter() != null
                    && metadata.getHostingResearchCenter().getResearchCenter() != null) {
                hostingCenterName = metadata.getHostingResearchCenter().getResearchCenter().getDisplayName();
            }
        }

        ServiceURL serviceURL = new ServiceURL();
        serviceURL.setHostingCenter(hostingCenterName);
        serviceURL.setDescription(description);
        serviceURL.setUrlLocation(serviceUrl);
        serviceURL.setEntityGroupName(entityGroupName);
        serviceURL.setAdminDefined(false);
        serviceURL.setConfigured(false);
        return serviceURL;
    }

    /**
     * It performs a two way merging.
     * If Service URL from Index Service contains Hosting Center Name and Description then Those are associated 
     * with that Service URL and returned. So changes are made in Database object and returned instead of Index Service
     * object as Database object has UrlId associated with it saved in database which is not the case with Index Service
     * URL.   
     * @param urlsFromDatabase
     * @param urlsFromIndexService
     * @return mergerd list of Service URLs
     */
    static ServiceURL merge(ServiceURL urlsFromDatabase, ServiceURL urlsFromIndexService) {

        String hostingCenterName = urlsFromIndexService.getHostingResearchCenter();
        if (!hostingCenterName.isEmpty() && hostingCenterName.length() > 0) {
            urlsFromDatabase.setHostingCenter(hostingCenterName);
        }
        String description = urlsFromIndexService.getModelDescription();
        if (!description.isEmpty() && description.length() > 0) {
            urlsFromDatabase.setDescription(description);
        }
        String domainModel = urlsFromIndexService.getDomainModel();
        if (urlsFromDatabase.getDomainModel() == null || urlsFromDatabase.getDomainModel().isEmpty()) {
            urlsFromDatabase.setDomainModel(domainModel);
        }
        String version = urlsFromIndexService.getVersion();
        if (urlsFromDatabase.getVersion() == null || urlsFromDatabase.getVersion().isEmpty()) {
            urlsFromDatabase.setVersion(version);
        }
        return urlsFromDatabase;
    }

}
