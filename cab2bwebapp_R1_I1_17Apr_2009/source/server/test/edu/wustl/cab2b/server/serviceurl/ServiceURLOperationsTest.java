package edu.wustl.cab2b.server.serviceurl;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;

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
        List<ServiceURLInterface> serviceURLs = serviceURLOperations.getAllURLsForEntityGroup("caArray_v2.1");
        System.out.println(serviceURLs.size());
        assertTrue(serviceURLs.size()>0);
    }
    
    public void testgetInstancesByServiceName() {
        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        UserInterface user =  new UserOperations().getUserById("1");
        serviceURLOperations.getInstancesByServiceName("caArray", "2.1", user);
    }

    private ServiceURLInterface getById(Long id) {
        Session session = HibernateUtil.newSession();
        try {
            HibernateDatabaseOperations<ServiceURLInterface> dbHandler = new HibernateDatabaseOperations<ServiceURLInterface>(
                    session);
            ServiceURLInterface serviceURL = dbHandler.retrieveById(ServiceURLInterface.class.getName(), 2L);
            return serviceURL;
        } finally {
            session.close();
        }
    }
}