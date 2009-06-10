package edu.wustl.cab2b.server.serviceurl;

import static edu.wustl.cab2b.common.util.Constants.CATEGORY_ENTITY_GROUP_NAME;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.util.dbManager.HibernateUtility;

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
        Collection<ServiceURLInterface> serviceList = getAllServiceURLs();
        Collection<String> applicationNames = new HashSet<String>();
        for (ServiceURLInterface serviceURL : serviceList) {
            applicationNames.add(serviceURL.getEntityGroupName());
        }
        //TODO temporarily adding constant here. LAter this should be a change in database
        applicationNames.add(CATEGORY_ENTITY_GROUP_NAME);

        return applicationNames;
    }

    /**
     * @return all the service URL present in the database
     * @throws RemoteException
     */
    public Collection<ServiceURLInterface> getAllServiceURLs() throws RemoteException {
        Collection<ServiceURLInterface> serviceURLs = null;

        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURLInterface> dbHandler = new HibernateDatabaseOperations<ServiceURLInterface>(
                session);

        serviceURLs = dbHandler.retrieve(ServiceURLInterface.class.getName());
        return serviceURLs;
    }

    /**
     * This method returns the ServiceURLInterface object for given URL Location
     * @param serviceURLLocation
     * @return
     * @throws RemoteException
     */
    public ServiceURLInterface getServiceURLbyURLLocation(String serviceURLLocation) throws RemoteException {
        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURLInterface> dbHandler = new HibernateDatabaseOperations<ServiceURLInterface>(
                session);
        Collection<ServiceURLInterface> serviceURLs = dbHandler.retrieve(ServiceURLInterface.class.getName(),
                                                                         "urlLocation", serviceURLLocation);

        ServiceURLInterface serviceURL = null;
        if (!serviceURLs.isEmpty()) {
            serviceURL = serviceURLs.iterator().next();
        }
        return serviceURL;
    }

    /**
     * This method returns the List of ServiceURLs for the given ServiceName and Version.
     * Also those URLs configured by the user are shown first.
     * These urls are fetched from database and not from Index Service
     * @param serviceName
     * @param version
     * @param user
     * @return List of service URLs for a particular Entity Group from database and Index Service
     * @throws RemoteException
     */
    public List<ServiceURLInterface> getInstancesByServiceName(String serviceName, String version,
                                                               UserInterface user) {
        String entityGroupName = Utility.createModelName(serviceName, version);

        List<ServiceURLInterface> allServices = getAllURLsForEntityGroup(entityGroupName);
        Map<String, List<String>> userConfiguredURLs = new UserOperations().getServiceURLsForUser(user);
        List<String> userConfiguredServiceUrls = userConfiguredURLs.get(entityGroupName);

        if (userConfiguredServiceUrls != null) {
            allServices = mergeURLs(allServices, userConfiguredServiceUrls);
        }
        Collections.sort(allServices, new ServiceSorter());

        return allServices;
    }

    /**
     * Sets isConfigured True for those URLs which are configured by that user before
     * @param allServices
     * @param userDefinedURL
     * @return
     */
    private List<ServiceURLInterface> mergeURLs(List<ServiceURLInterface> allServices, List<String> userDefinedURL) {
        for (ServiceURLInterface serviceUrl : allServices) {
            if (userDefinedURL.contains(serviceUrl.getUrlLocation())) {
                serviceUrl.setConfigured(true);
            }
        }
        return allServices;
    }

    /**
     * This method will return a collection of all the service instances present in the database for that
     * domain model and version.
     * @param entityGroupName
     * @return
     */
    public List<ServiceURLInterface> getAllURLsForEntityGroup(String entityGroupName) {
        List<ServiceURLInterface> serviceURLs = new ArrayList<ServiceURLInterface>();
        try {
            Collection<ServiceURLInterface> serviceURLList = HibernateUtility.executeHQL(
                                                                                         "getServiceURLsByDomainModelnVersion",
                                                                                         Arrays.asList((Object[]) entityGroupName.split("_v")));
            serviceURLs.addAll(serviceURLList);
        } catch (HibernateException e) {
            logger.info(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return serviceURLs;
    }

    /**
     * This method will save ServiceURL HO to database
     * @param serviceURL
     */
    public void saveServiceURL(ServiceURLInterface serviceURL) {
        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURLInterface> dbHandler = new HibernateDatabaseOperations<ServiceURLInterface>(
                session);
        dbHandler.insertOrUpdate(serviceURL);
        session.flush();
        session.close();
    }

    /**
     * Updates current user object with newly selected/unselected Service Instances.
     * @param entityGroupName
     * @param serviceURLObjects
     * @param currentUser
     */
    public UserInterface saveServiceInstances(String entityGroupName,
                                              Collection<ServiceURLInterface> serviceURLObjects,
                                              UserInterface currentUser) {
        Collection<ServiceURLInterface> allServiceURLList = currentUser.getServiceURLCollection();
        if (serviceURLObjects.isEmpty()) {
            Collection<ServiceURLInterface> serviceURLsToRemove = new HashSet<ServiceURLInterface>();

            for (ServiceURLInterface serviceURL : allServiceURLList) {
                String serviceName = serviceURL.getEntityGroupName();
                if (entityGroupName.equalsIgnoreCase(serviceName)) {
                    serviceURLsToRemove.add(serviceURL);
                }
            }
            allServiceURLList.removeAll(serviceURLsToRemove);
            return currentUser;
        }

        Collection<ServiceURLInterface> newServices = new HashSet<ServiceURLInterface>(serviceURLObjects);
        Collection<ServiceURLInterface> serviceURLsToRemove = new HashSet<ServiceURLInterface>();
        for (ServiceURLInterface serviceURL : allServiceURLList) {
            String serviceName = serviceURL.getEntityGroupName();
            if (entityGroupName.equalsIgnoreCase(serviceName)) {
                if (newServices.contains(serviceURL)) {
                    newServices.remove(serviceURL);
                } else {
                    serviceURLsToRemove.add(serviceURL);
                }
            }

        }
        allServiceURLList.removeAll(serviceURLsToRemove);
        allServiceURLList.addAll(newServices);

        return currentUser;
    }
}

/**
 * Comparator class for sorting service instance URL object
 * 	depending upon isConfigured status and aplhabetical order.
 * @author gaurav_mehta
 */
class ServiceSorter implements Comparator<ServiceURLInterface> {

    public int compare(ServiceURLInterface obj1, ServiceURLInterface obj2) {

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