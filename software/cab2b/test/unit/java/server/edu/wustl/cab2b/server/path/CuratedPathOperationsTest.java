/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import java.util.List;

import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.cab2b.server.util.TestConnectionUtil;
import edu.wustl.cab2b.server.util.TestUtil;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.util.logger.Logger;
import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class CuratedPathOperationsTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        Logger.configure();
        EntityCache.getInstance();
        PathFinder.getInstance(TestConnectionUtil.getConnection());
    }

    public void testGetAllCuratedPath() {
        CuratedPathOperations opr = new CuratedPathOperations();
        try {
            List<ICuratedPath> list = opr.getAllCuratedPath();
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Exception in getAllCuratedPath()");
        }
    }

//    public void testIsDuplicate() {
//        CuratedPathOperations opr = new CuratedPathOperations();
//
//        List<ICuratedPath> list = opr.getAllCuratedPath();
//        if (!list.isEmpty()) {
//            boolean res = opr.isDuplicate(list.get(0));
//            assertTrue(res);
//
//        }
//    }

    public void testGetPathById() {
        CuratedPathOperations opr = new CuratedPathOperations();
        try {
            IPath path = opr.getPathById(1L);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Exception in getPathById()");
        }
    }
}
