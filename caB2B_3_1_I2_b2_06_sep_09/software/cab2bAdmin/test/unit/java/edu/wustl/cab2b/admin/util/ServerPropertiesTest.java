package edu.wustl.cab2b.admin.util;

import edu.wustl.cab2b.common.authentication.util.CagridPropertyLoader;
import junit.framework.TestCase;

/**
 * @author Chandrakant Talele
 */
public class ServerPropertiesTest extends TestCase {
    public void testGetCadsrRefreshTime() {
        String str = CagridPropertyLoader.getCaDSRRefreshTime();
        assertEquals(18000000L,Long.parseLong(str));
    }
}
