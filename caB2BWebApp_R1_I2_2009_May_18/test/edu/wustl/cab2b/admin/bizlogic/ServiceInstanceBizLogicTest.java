package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.axis.types.URI.MalformedURIException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author Chandrakant Talele
 */
public class ServiceInstanceBizLogicTest extends TestCase {

    public UserInterface getAdmin() {
        return new UserOperations().getAdmin();
    }

    public void testGetServiceMetadataObjects() throws MalformedURIException, RemoteException {
        Set<String> urls = new HashSet<String>();
        urls.add("http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect");
        ServiceInstanceBizLogic bizLogic = new ServiceInstanceBizLogic();
        List<ServiceURLInterface> list = bizLogic.getServiceMetadataObjects("GeneConnect", "1", getAdmin());
        for (ServiceURLInterface m : list) {
            urls.remove(m.getUrlLocation().toString());
        }
        assertTrue(urls.isEmpty());
    }

    public void testGetMetadataEntityGroups() {
        ServiceInstanceBizLogic logic = new ServiceInstanceBizLogic();
        Collection<EntityGroupInterface> groups = logic.getMetadataEntityGroups();
        for (EntityGroupInterface group : groups) {
            if (Constants.CATEGORY_ENTITY_GROUP_NAME.equalsIgnoreCase(group.getLongName())) {
                assertTrue("getMetadataEntityGroups() returned category entity group. It should filter it", false);
            }
        }
    }

    public void testSaveServiceInstances() throws MalformedURIException, RemoteException {
        User admin = (User) getAdmin();
        ServiceInstanceBizLogic logic = new ServiceInstanceBizLogic();
        List<ServiceURLInterface> list = logic.getServiceMetadataObjects("GeneConnect", "1", admin);

        UserInterface updatedUser = logic.saveServiceInstances(list.get(0).getEntityGroupName(), list, admin);
        boolean urlSavedSuccessfully = false;
        Collection<ServiceURLInterface> urls = updatedUser.getServiceURLCollection();
        for (ServiceURLInterface url : urls) {
            if (url.equals(list.get(0))) {
                urlSavedSuccessfully = true;
            }
        }
        assertTrue(urlSavedSuccessfully);
    }

}
