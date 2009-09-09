/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroup;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * JUnitTest case for SaveServiceInstancesBizLogic class
 * @author deepak_shingan
 *
 */
public class SaveServiceInstancesBizLogicTest extends TestCase {
    private SaveServiceInstancesBizLogic bizLogic = new SaveServiceInstancesBizLogic();

    /**
     * Test method for Test method for updateServiceInstanceSettings() method.
     * when user parameter object is null.
     */
    public final void testUpdateServiceInstanceSettings_UserNull() {
        List<String> userSelectedURLs = new ArrayList<String>();
        List<ServiceURLInterface> serviceInstances = new ArrayList<ServiceURLInterface>();
        String modelGroupName = "MicroArrayData";

        UserInterface user = bizLogic.updateServiceInstanceSettings(null, userSelectedURLs, serviceInstances,
                                                                    modelGroupName);
        assertNull("For null user object return user object should be null", user);
    }

    /**
     * Test method for Test method for updateServiceInstanceSettings() method.
     * when SelectedURL list is null.
     */
    public final void testUpdateServiceInstanceSettings_NullSelectedURLs() {
        List<ServiceURLInterface> serviceInstances = new ArrayList<ServiceURLInterface>();
        String modelGroupName = "MicroArrayData";

        //create ANONYMOUS user
        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);

        //for ANONYMOUS user initial configured service URLS will be Admin configured
        int adminConfigured = user.getServiceURLCollection().size();
        user = bizLogic.updateServiceInstanceSettings(user, null, serviceInstances, modelGroupName);
        assertEquals(
                     "If user does not select any urls for current user, user.getServiceURLCollection() method size should equal to Admin configured",
                     adminConfigured, user.getServiceURLCollection().size());
    }

    /**
     * Test method for Test method for updateServiceInstanceSettings() method.
     * when modelGroupName is empty.
     */
    public final void testUpdateServiceInstanceSettings_NullServiceInstances() {
        List<String> userSelectedURLs = new ArrayList<String>();
        String modelGroupName = "";

        //create ANONYMOUS user
        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);

        //for ANONYMOUS user initial configured service URLS will be Admin configured
        int adminConfigured = user.getServiceURLCollection().size();
        user = bizLogic.updateServiceInstanceSettings(user, userSelectedURLs, null, modelGroupName);
        assertEquals(
                     "If user does not have any serviceUrls available in database, user.getServiceURLCollection() method size should be same as previous",
                     adminConfigured, user.getServiceURLCollection().size());
    }

    /**
     * Test method for Test method for updateServiceInstanceSettings() method.
     * when modelGroupName is empty.
     */
    public final void testUpdateServiceInstanceSettings_NullModelGroup() {
        List<String> userSelectedURLs = new ArrayList<String>();
        List<ServiceURLInterface> allServiceInstances = new ArrayList<ServiceURLInterface>();
        String modelGroupName = "";

        //create ANONYMOUS user
        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);

        //for ANONYMOUS user initial configured service URLS will be Admin configured
        int adminConfigured = user.getServiceURLCollection().size();
        user = bizLogic.updateServiceInstanceSettings(user, userSelectedURLs, allServiceInstances, modelGroupName);
        assertEquals(
                     "If user does not select any modelGroupName, user.getServiceURLCollection() method size should be same as previous",
                     adminConfigured, user.getServiceURLCollection().size());
    }

    /**
     * Test method for updateServiceInstanceSettings() method
     */
    public final void testUpdateServiceInstanceSettings() {
        //String entityGroupName = "caArray_v2.1";
        String entityGroupName = "GeneConnect_v1";
        
        Collection<EntityGroupInterface> groups = EntityCache.getInstance().getEntityGroups();
        EntityGroupInterface geneConnect = null;
        for (EntityGroupInterface eg : groups) {
            if (eg.getName().equals(entityGroupName)) {
                geneConnect = eg;
            }
        }
        assertNotNull("This testcase expect Geneconnect to be present into database",geneConnect);
        List<EntityGroupInterface> entitygroupList = new ArrayList<EntityGroupInterface>();
        entitygroupList.add(geneConnect);
        
        ModelGroupInterface mg = new ModelGroup();
        mg.setModelGroupName("GeneConnect Data" + System.currentTimeMillis());
        mg.setSecured(false);
        mg.setEntityGroupList(entitygroupList);
        new ModelGroupOperations().saveModelGroup(mg);

        String url = "http://128.252.227.94:9092/wsrf/services/cagrid/GeneConnect";
        ServiceURL serviceUrl = new ServiceURL();
        serviceUrl.setUrlLocation(url);
        
        List<ServiceURLInterface> allServiceInstances = new ArrayList<ServiceURLInterface>();
        allServiceInstances.add(serviceUrl);

        List<String> userSelectedURLs = new ArrayList<String>();
        userSelectedURLs.add(url);
        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);

        SaveServiceInstancesBizLogic bizLogic = new SaveServiceInstancesBizLogic();
        user = bizLogic.updateServiceInstanceSettings(user, userSelectedURLs, allServiceInstances, mg.getModelGroupName());

        Collection<ServiceURLInterface> updatedConfiguration = user.getServiceURLCollection();
        assertNotNull(updatedConfiguration);
        assertEquals(1, updatedConfiguration.size());
        ServiceURLInterface serUrl = updatedConfiguration.iterator().next();
        assertTrue(serUrl.isConfigured());
        assertEquals(url, serUrl.getUrlLocation());
    }
}
