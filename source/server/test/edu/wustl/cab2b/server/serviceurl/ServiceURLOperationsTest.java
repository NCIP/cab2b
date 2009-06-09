package edu.wustl.cab2b.server.serviceurl;

import java.rmi.RemoteException;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;

import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;

/**
 * @author gaurav_mehta
 *
 */
public class ServiceURLOperationsTest extends TestCase {
    public void testSave() {

        ServiceURLInterface serviceURL = getById(1L);
        System.out.println(serviceURL.getUrlLocation());
        serviceURL.setHostingCenter("Hosted at C-21 3");

        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        serviceURLOperations.saveServiceURL(serviceURL);
    }

    public void testgetAllURLsForEntityGroup() {
        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        List<ServiceURLInterface> serviceURLs = serviceURLOperations.getAllURLsForEntityGroup("GeneConnect_v1");
        System.out.println(serviceURLs.size());
        assertTrue(serviceURLs.size() > 0);
    }

    public void testgetInstancesByServiceName() {
        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        UserInterface user = new UserOperations().getUserById("1");
        List<ServiceURLInterface> serviceURL = serviceURLOperations.getInstancesByServiceName("GeneConnect", "1", user);
        assertTrue(serviceURL.size()>0);
    }

    public void testgetServiceURLbyURLLocation() throws RemoteException {
        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        ServiceURLInterface serviceURL = serviceURLOperations.getServiceURLbyURLLocation("http://wucabig1.wustl.edu:9092/wsrf/services/cagrid/GeneConnect");
        assertEquals("GeneConnect", serviceURL.getDomainModel());
    }

    private ServiceURLInterface getById(Long id) {
        Session session = HibernateUtil.newSession();
        try {
            HibernateDatabaseOperations<ServiceURLInterface> dbHandler = new HibernateDatabaseOperations<ServiceURLInterface>(
                    session);
            ServiceURLInterface serviceURL = dbHandler.retrieveById(ServiceURL.class.getName(), 2L);
            return serviceURL;
        } finally {
            session.close();
        }
    }
}