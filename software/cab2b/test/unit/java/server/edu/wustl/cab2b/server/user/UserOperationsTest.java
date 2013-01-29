/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.user;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * @author chandrakant_talele
 */
public class UserOperationsTest extends TestCase {
	static final String name = "Admin";

	public void testGetUserByName() {

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
		UserInterface user = userOperations.getUserByName(name);

		Map<String, List<String>> res = userOperations.getServiceURLsForUser(user);
		assertNotNull(res);
	}

	public void testGetAdmin() {
		UserOperations userOperations = new UserOperations();
		UserInterface user = userOperations.getAdmin();
		assertEquals(name, user.getUserName());
	}

	public void testInsertUser() {
		UserOperations userOperations = new UserOperations();
		UserInterface user = userOperations.getAdmin();
		boolean gotException = false;
		try {
			userOperations.insertUser(user);
		} catch (RuntimeException e) {
			gotException = true;
		}
		assertTrue(gotException);
	}

	public void testUpdateUser() {
		UserOperations userOperations = new UserOperations();
		UserInterface user = userOperations.getAdmin();
		boolean gotException = false;
		try {
			userOperations.updateUser(user);
		} catch (RuntimeException e) {
			gotException = true;
		}
		assertFalse(gotException);
	}
	
	public void testGetServiceURLsForUserForAdminUrls() {
		UserOperations userOperations = new UserOperations();
		UserInterface user = userOperations.getUserByName("Anonymous");

		Map<String, List<String>> anonymousUser = userOperations.getServiceURLsForUser(user);
		Map<String, List<String>> adminUser = userOperations.getServiceURLsForUser(userOperations.getAdmin());
		assertEquals(anonymousUser.size(), adminUser.size());
	}
}
