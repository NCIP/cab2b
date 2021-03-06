/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import java.sql.Connection;

import javax.naming.NamingException;

import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class ConnectionUtilTest extends TestCase {
    public void testGetConnectionNamingException() {
        try {
            ConnectionUtil.getConnection();
            fail("NamingException should have been thrown");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof NamingException);
        }
    }

    public void testCloseConnection() {
        Connection con = TestConnectionUtil.getConnection();
        ConnectionUtil.close(con);
        ConnectionUtil.close(con); // this will throw SQLException
    }
}
