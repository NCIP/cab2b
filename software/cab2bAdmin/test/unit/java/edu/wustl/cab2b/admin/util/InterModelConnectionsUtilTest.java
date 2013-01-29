/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.util;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class InterModelConnectionsUtilTest extends TestCase {
    public void testDetermineConnectionsForException() {
        EntityGroupInterface eg1 = TestUtil.getEntityGroup("eg1", 1L);
        EntityInterface en1 = TestUtil.getEntity("en1", 101L);
        en1.addEntityGroupInterface(eg1);

        EntityInterface en2 = TestUtil.getEntity("en2", 102L);
        en2.addEntityGroupInterface(eg1);
        boolean gotException = false;
        try {
            InterModelConnectionsUtil.determineConnections(en1, en2);
        } catch (IllegalArgumentException e) {
            gotException = true;
        }
        assertTrue(gotException);
    }
    //        public void testDetermineConnectionsFor() {
    //    
    //            EntityGroupInterface eg1 = TestUtil.getEntityGroup("eg1", 1L);
    //            EntityGroupInterface eg2 = TestUtil.getEntityGroup("eg2", 2L);
    //    
    //            EntityInterface en1 = TestUtil.getEntity("en1", 101L);
    //            en1.addEntityGroupInterface(eg1);
    //    
    //            EntityInterface en2 = TestUtil.getEntity("en2", 102L);
    //            en2.addEntityGroupInterface(eg2);
    //    
    //            Set<AttributePair> pairs = InterModelConnectionsUtil.determineConnections(en1, en2);
    //        }
}
