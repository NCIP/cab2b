package edu.wustl.cab2b.server.serviceurl;

import static edu.wustl.cab2b.common.util.Constants.CATEGORY_ENTITY_GROUP_NAME;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.cab2b.common.user.ServiceURL;
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
        Collection<ServiceURL> serviceList = getAllServiceURLs();
        Collection<String> applicationNames = new HashSet<String>();
        for (ServiceURL serviceURL : serviceList) {
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
    public Collection<ServiceURL> getAllServiceURLs() throws RemoteException {
        Collection<ServiceURL> serviceURLs = null;

        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURL> dbHandler = new HibernateDatabaseOperations<ServiceURL>(session);

        serviceURLs = dbHandler.retrieve(ServiceURL.class.getName());
        return serviceURLs;
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
    public List<ServiceURL> getInstancesByServiceName(String serviceName, String version, UserInterface user) {
        List<ServiceURL> allServices = null;
        String entityGroupName = Utility.createModelName(serviceName, version);
        Map<String, List<String>> userConfiguredURLs = new HashMap<String, List<String>>();
        List<String> userConfiguredServiceUrls = null;

        allServices = getAllURLsForEntityGroup(entityGroupName);
        userConfiguredURLs = new UserOperations().getServiceURLsForUser(user);
        userConfiguredServiceUrls = userConfiguredURLs.get(entityGroupName);

        if (userConfiguredServiceUrls != null) {
            allServices = mergeURls(allServices, userConfiguredServiceUrls);
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
    private List<ServiceURL> mergeURls(List<ServiceURL> allServices, List<String> userDefinedURL) {
        for (ServiceURL serviceUrl : allServices) {
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
    public List<ServiceURL> getAllURLsForEntityGroup(String entityGroupName) {
        List<ServiceURL> serviceURLs = new ArrayList<ServiceURL>();
        try {
            Collection<ServiceURL> serviceURLList = HibernateUtility.executeHQL(
                                                                                "getServiceURLsByDomainModelnVersion",
                                                                                Arrays.asList((Object[]) entityGroupName.split("_v")));
            for (ServiceURL serviceURL : serviceURLList) {
                serviceURLs.add(serviceURL);
            }
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
    public void saveServiceURL(ServiceURL serviceURL) {
        Session session = HibernateUtil.newSession();
        HibernateDatabaseOperations<ServiceURL> dbHandler = new HibernateDatabaseOperations<ServiceURL>(session);
        dbHandler.insertOrUpdate(serviceURL);
        session.flush();
        session.close();
    }

    /**
     * @param entityGroupName
     * @param serviceURLObjects
     * @param currentUser
     */
    public UserInterface saveServiceInstances(String entityGroupName, Collection<ServiceURL> serviceURLObjects,
                                     UserInterface currentUser) {
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
            return currentUser;
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
        return currentUser;
    }
}

/**
 * @author gaurav_mehta
 *
 */
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