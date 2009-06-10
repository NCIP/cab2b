package edu.wustl.cab2b.server.user;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author chandrakant_talele
 */
public class UserOperationsTest extends TestCase {
    public void testGetUserByName() {
        String name = "Admin";
        UserOperations userOperations = new UserOperations();
        UserInterface user = userOperations.getUserByName(name);
        assertEquals(name, user.getUserName());
    }

    public void testGetUserById() {
        String id = "1";
        UserOperations userOperations = new UserOperations();
        UserInterface user = userOperations.getUserById(id);

        assertEquals(Long.parseLong(id), user.getUserId().longValue());
    }
    public void testGetServiceURLsForUser() {
        UserOperations userOperations = new UserOperations();
        UserInterface user = userOperations.getUserByName("Admin");

        Map<String, List<String>> res = userOperations.getServiceURLsForUser(user);
        assertNotNull(res);
//        for(String appName : res.keySet()) {
//           System.out.println(appName + " : " + res.get(appName));
//        }
    }
}
