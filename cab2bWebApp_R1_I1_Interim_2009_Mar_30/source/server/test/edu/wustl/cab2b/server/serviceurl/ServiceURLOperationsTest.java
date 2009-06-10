package edu.wustl.cab2b.server.serviceurl;
import java.util.List;

import org.hibernate.Session;

import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.serviceurl.ServiceURLOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class ServiceURLOperationsTest extends TestCase {
    public void testSave() {

        ServiceURL serviceURL = getById(1L);
        System.out.println(serviceURL.getUrlLocation());
        serviceURL.setHostingCenter("Hosted at C-21 3");

        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        serviceURLOperations.saveServiceURL(serviceURL);
    }
    
    public void testgetAllURLsForEntityGroup() {
        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        List<ServiceURL> serviceURLs = serviceURLOperations.getAllURLsForEntityGroup("caArray_v2.1");
        System.out.println(serviceURLs.size());
        assertTrue(serviceURLs.size()>0);
    }
    
    public void testgetInstancesByServiceName() {
        ServiceURLOperations serviceURLOperations = new ServiceURLOperations();
        UserInterface user =  new UserOperations().getUserById("1");
        serviceURLOperations.getInstancesByServiceName("caArray", "2.1", user);
    }

    private ServiceURL getById(Long id) {
        Session session = HibernateUtil.newSession();
        try {
            HibernateDatabaseOperations<ServiceURL> dbHandler = new HibernateDatabaseOperations<ServiceURL>(
                    session);
            ServiceURL serviceURL = dbHandler.retrieveById(ServiceURL.class.getName(), 2L);
            return serviceURL;
        } finally {
            session.close();
        }
    }
}