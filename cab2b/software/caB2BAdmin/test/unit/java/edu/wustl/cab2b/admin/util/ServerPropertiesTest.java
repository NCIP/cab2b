package edu.wustl.cab2b.admin.util;

import junit.framework.TestCase;

/**
 * @author Chandrakant Talele
 */
public class ServerPropertiesTest extends TestCase {
    public void testGetCadsrRefreshTime() {
        String str = ServerProperties.getCadsrRefreshTime();
        assertEquals(18000000L,Long.parseLong(str));
    }
}
