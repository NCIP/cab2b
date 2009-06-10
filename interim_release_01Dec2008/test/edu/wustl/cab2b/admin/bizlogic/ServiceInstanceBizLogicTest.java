package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.axis.types.URI.MalformedURIException;

import edu.wustl.cab2b.common.user.AdminServiceMetadata;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author Chandrakant Talele
 */
public class ServiceInstanceBizLogicTest extends TestCase {

    public UserInterface getUser() {
        UserOperations userOperation = new UserOperations();
        return userOperation.getUserByName("Admin");
    }

    public void testGeneConnect() throws MalformedURIException, RemoteException {
        Set<String> urls = new HashSet<String>();
        urls.add("http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect");
        ServiceInstanceBizLogic bizLogic = new ServiceInstanceBizLogic();
        List<AdminServiceMetadata> list = bizLogic.getServiceMetadataObjects("GeneConnect",null, getUser());
        for (AdminServiceMetadata m : list) {
            urls.remove(m.getServiceURL().toString());
        }
        assertTrue(urls.isEmpty());
    }
}
