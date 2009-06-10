/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
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
        List<String> userSelectedURLs = new ArrayList<String>();
        List<ServiceURLInterface> allServiceInstances = new ArrayList<ServiceURLInterface>();
        String modelGroupName = "MicroArrayData";

        //create ANONYMOUS user
        UserInterface user = new UserOperations().getUserByName(Constants.ANONYMOUS);

        //for ANONYMOUS user initial configured service URLS will be Admin configured        
        Collection<ServiceURLInterface> allAdminConfiguredUrls = user.getServiceURLCollection();
        Iterator<ServiceURLInterface> iter = allAdminConfiguredUrls.iterator();
        String configuredURL = ((ServiceURLInterface) iter.next()).getUrlLocation();
        //only add first url location as selected 
        if (iter.hasNext()) {
            userSelectedURLs.add(configuredURL);
        }

        user = bizLogic.updateServiceInstanceSettings(user, userSelectedURLs, allServiceInstances, modelGroupName);

        Collection<ServiceURLInterface> updatedConfiguration = user.getServiceURLCollection();
        boolean isConfigured = false;
        for (ServiceURLInterface serUrl : updatedConfiguration) {
            if (isConfigured = serUrl.getUrlLocation().equals(configuredURL)) {
                break;
            }
        }
        assertEquals(
                     "If user has selected only one serviceInstanceUrl,so user.getServiceURLCollection() method size should show only one url as configured",
                     true, isConfigured);
    }
}
