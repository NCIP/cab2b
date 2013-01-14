/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.serviceurl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.authentication.util.CagridPropertyLoader;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
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
    private static AtomicBoolean isStarted = new AtomicBoolean(false);

    /**
     * Private Constructor of the class
     */
    private IndexServiceOperations() {
    }

    /** As of now this method is invoked only by the context listeners of caB2B-Admin ,caB2BLite
     * and first EJB call of caB2B. This will be changed once all three are merged into one common war
     *  It starts the thread for refreshing the ServiceURL database.
     */
    public static void refreshDatabase() {
        if (!isStarted.getAndSet(true)) {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    logger.info("Refreshing Database Started..........");
                    Collection<EntityGroupInterface> entityCollection = new ServiceURLUtility().getMetadataEntityGroups(EntityCache.getInstance());
                    for (EntityGroupInterface entityGroup : entityCollection) {
                        try {
                            refreshServiceURLsMetadata(entityGroup.getLongName(), entityGroup.getVersion());
                        } catch (MalformedURIException e) {
                            logger.error(e.getMessage(), e);
                        } catch (RemoteException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    logger.info("Database has been refreshed and is in Sync with Index Service");
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 3600000 * 12);
        }
    }

    /** This method gets all the ServiceURLs from database for that EntityGroup and also from IndexService.
     * It adds them in a single Set so that URLs are not repeated as set doesn't allow storage of repeated entry
     * @param domainModel
     * @param version
     * @throws RemoteException 
     * @throws MalformedURIException 
     */
    public static void refreshServiceURLsMetadata(String domainModel, String version)
            throws MalformedURIException, RemoteException {
        String entityGroupName = Utility.createModelName(domainModel, version);

        logger.info("Refreshing Service Instances Metadata for " + entityGroupName + "..........");
        ServiceURLOperations serviceUrlOperation = new ServiceURLOperations();
        Set<ServiceURLInterface> urlsfromIndexService = new HashSet<ServiceURLInterface>();
        Set<ServiceURLInterface> refreshedURLMetadata = new HashSet<ServiceURLInterface>();

        Set<ServiceURLInterface> urlsfromDatabase = new HashSet<ServiceURLInterface>(
                serviceUrlOperation.getAllURLsForEntityGroup(entityGroupName));

        urlsfromIndexService = getServiceURLsfromIndex(domainModel, version);

        urlsfromDatabase.addAll(urlsfromIndexService);
        refreshedURLMetadata = refreshMetadata(urlsfromDatabase, entityGroupName);
        for (ServiceURLInterface serviceURL : refreshedURLMetadata) {
            serviceUrlOperation.saveServiceURL(serviceURL);
        }
        serviceUrlOperation.updateCache();
    }

    /**
     * This method returns the ServiceURLs from Index Service for given Service and its Version
     * @param domainModel
     * @param version
     * @return
     * @throws MalformedURIException
     * @throws RemoteException
     */
    private static Set<ServiceURLInterface> getServiceURLsfromIndex(String domainModel, String version)
            throws MalformedURIException, RemoteException {

        String urls = CagridPropertyLoader.getIndexServiceUrl();
        Set<ServiceURLInterface> setofServiceURLs = new HashSet<ServiceURLInterface>();
        DiscoveryClientForMetaData client = new DiscoveryClientForMetaData(urls);
        EndpointReferenceType[] endPointRefTypeArray;

        try {
            endPointRefTypeArray = client.discoverDataServicesByDomainModel(domainModel, version);
            if (endPointRefTypeArray != null) {
                for (EndpointReferenceType endPoint : endPointRefTypeArray) {
                    ServiceURLInterface serviceurl = new ServiceURL();
                    serviceurl.setUrlLocation((endPoint.getAddress().toString()));
                    setofServiceURLs.add(serviceurl);
                }
            }
        } catch (RemoteResourcePropertyRetrievalException e) {
            logger.error(e.getMessage(), e);
            throw new RemoteException(e.getMessage());
        } catch (QueryInvalidException e) {
            logger.error(e.getMessage(), e);
            throw new RemoteException(e.getMessage());
        } catch (ResourcePropertyRetrievalException e) {
            logger.error(e.getMessage(), e);
            throw new RemoteException(e.getMessage());
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
    private static Set<ServiceURLInterface> refreshMetadata(Set<ServiceURLInterface> urlsfromDatabase,
                                                            String entityGroupName) {
        Set<ServiceURLInterface> refreshedMetadata = new HashSet<ServiceURLInterface>();
        for (ServiceURLInterface urlMetatadatafromDatabase : urlsfromDatabase) {
            String urlLocation = urlMetatadatafromDatabase.getUrlLocation();
            ServiceURLInterface urlMetadatafromIndexService = new ServiceURL();
            try {
                urlMetadatafromIndexService = getMetadata(urlLocation, entityGroupName);
            } catch (MalformedURIException e) {
                logger.error(e.getMessage(), e);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
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
    private static ServiceURLInterface getMetadata(String serviceUrl, String entityGroupName)
            throws MalformedURIException, RemoteException {
        String urls = CagridPropertyLoader.getIndexServiceUrl();
        DiscoveryClientForMetaData client = new DiscoveryClientForMetaData(urls);
        ServiceMetadata metadata = new ServiceMetadata();
        try {
            metadata = client.discoverDataServiceByURL(serviceUrl);
        } catch (ResourcePropertyRetrievalException e) {
            logger.error(e.getMessage(), e);
            throw new RemoteException(e.getMessage());
        }

        return new ServiceMetadataProcessor().getServiceMetadata(metadata, serviceUrl, entityGroupName);
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
    private static ServiceURLInterface merge(ServiceURLInterface urlsFromDatabase,
                                             ServiceURLInterface urlsFromIndexService) {

        String hostingCenterName = urlsFromIndexService.getHostingResearchCenter();
        if (hostingCenterName != null && hostingCenterName.length() != 0) {
            urlsFromDatabase.setHostingCenter(hostingCenterName);
        }

        String description = urlsFromIndexService.getServiceDescription();
        if (description != null && description.length() != 0) {
            urlsFromDatabase.setDescription(description);
        }
        String domainModel = urlsFromIndexService.getDomainModel();
        String version = urlsFromIndexService.getVersion();
        if (domainModel != null && domainModel.length() != 0 && version != null && version.length() != 0) {
            String entityGroupName = Utility.createModelName(domainModel, version);
            urlsFromDatabase.setEntityGroupName(entityGroupName);
        }

        String contactName = urlsFromIndexService.getContactName();
        if (contactName != null && contactName.length() != 0) {
            urlsFromDatabase.setContactName(contactName);
        }

        String contactMailId = urlsFromIndexService.getContactMailId();
        if (contactMailId != null && contactMailId.length() != 0) {
            urlsFromDatabase.setContactMailId(contactMailId);
        }
        String hostingCenterShortName = urlsFromIndexService.getHostingCenterShortName();
        if (hostingCenterShortName != null && hostingCenterShortName.length() != 0) {
            urlsFromDatabase.setHostingCenterShortName(hostingCenterShortName);
        }
        return urlsFromDatabase;
    }

}
