/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.server.util;

import java.util.Collection;

import junit.framework.TestCase;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.server.cache.EntityCache;

/**
 * @author gaurav_mehta
 *
 */
public class UtilityOperationsTest extends TestCase {

    public void testgetModelGroups() {
        EntityCache cache = EntityCache.getInstance();
        EntityGroupInterface entityGroup = cache.getEntityGroupByName("caTissue_Core_1_2_v1.2");
        Collection<ModelGroupInterface> modelGroups =
                UtilityOperations.getModelGroups(entityGroup.getEntityCollection().iterator().next());
        assertTrue(modelGroups.size() > 0);
        for (ModelGroupInterface mg : modelGroups) {
            if (!mg.getEntityGroupList().contains(entityGroup)) {
                assertFalse("Given Entity Group is not a part of Model group", Boolean.FALSE);
            }
        }
    }

}
